package com.bytes.app.base

import android.accounts.NetworkErrorException
import com.bytes.app.network.ErrorWrapper
import com.bytes.app.network.HttpCommonMethod
import com.bytes.app.network.HttpErrorCode
import com.bytes.app.network.ResponseHandler
import com.bytes.app.utils.Constants.JSON
import com.bytes.app.utils.DebugLog
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * Common class for API calling
 */

open class BaseRepository {

    /**
     * This is the Base suspended method which is used for making the call of an Api and
     * Manage the Response with response code to display specific response message or code.
     * @param call ApiInterface method defination to make a call and get response from generic Area.
     */
    suspend fun <T : Any> makeAPICall(call: suspend () -> Response<T>): ResponseHandler<T?> {
        var response: Response<T>? = null
        var rootCause: Exception? = null
        return withContext(Dispatchers.IO) {
            //emit response
            val res = flow { emit(call.invoke()) }
            //if error response than make api call attempt with delay
            res.retryWhen { cause, attempt ->
                if (cause != null && cause is Exception) {
                    rootCause = cause
                }
                if (cause is Exception && attempt < 3) {
                    delay(2000)
                    return@retryWhen true
                } else {
                    return@retryWhen false
                }
            }
                //catch exception
                .catch {
                    DebugLog.e("Error: ${it.message}")
                }
                //collect response
                .collect {
                    response =
                        Response.success(it.body()) //convert ResponseData<T> to Response<ResponseData<T>> & set response
                }
            try {
                if (response != null) {
                    if (response?.code() in (200..300)) {
                        return@withContext when (response?.code()) {
                            400 -> {

                                ResponseHandler.OnFailed(
                                    response?.code()!!,
                                    response?.message().toString(),
                                    "0"
                                )
                            }
                            401 -> {
                                ResponseHandler.OnFailed(
                                    HttpErrorCode.UNAUTHORIZED.code,
                                    response!!.message(),
                                    response!!.code().toString()
                                )
                            }
                            else -> ResponseHandler.OnSuccessResponse(response?.body())
                        }
                    } else if (response?.code() == 401) {
                        return@withContext parseUnAuthorizeResponse(response?.errorBody())
                    } else if (response?.code() == 422) {
                        return@withContext parseServerSideErrorResponse(response?.errorBody())
                    } else if (response?.code() == 500) {
                        return@withContext ResponseHandler.OnFailed(
                            HttpErrorCode.NOT_DEFINED.code,
                            "",
                            response!!.code().toString()
                        )
                    } else {
                        return@withContext parseUnKnownStatusCodeResponse(response?.errorBody())
                    }
                } else {
                    if (rootCause is UnknownHostException || rootCause is ConnectionShutdownException) {
                        ResponseHandler.OnFailed(HttpErrorCode.NO_CONNECTION.code, "", "")
                    } else {
                        ResponseHandler.OnFailed(HttpErrorCode.NOT_DEFINED.code, "", "")
                    }
                }
            } catch (e: Exception) {
                DebugLog.print(e)
                return@withContext if (
                    e is UnknownHostException ||
                    e is ConnectionShutdownException
                ) {
                    ResponseHandler.OnFailed(HttpErrorCode.NO_CONNECTION.code, "", "")
                } else if (e is SocketTimeoutException || e is IOException ||
                    e is NetworkErrorException
                ) {
                    ResponseHandler.OnFailed(HttpErrorCode.NOT_DEFINED.code, "", "")
                } else {
                    ResponseHandler.OnFailed(HttpErrorCode.NOT_DEFINED.code, "", "")
                }
            }
        }
    }

    /**
     * Response parsing for 401 status code
     **/
    private fun parseUnAuthorizeResponse(response: ResponseBody?): ResponseHandler.OnFailed {
        val message: String
        val bodyString = response!!.string()
        val responseWrapper: ErrorWrapper = JSON.readValue(bodyString)
        message = if (responseWrapper.meta!!.statusCode == 401) {
            if (responseWrapper.errors != null) {
                HttpCommonMethod.getErrorMessage(responseWrapper.errors)
            } else {
                responseWrapper.meta.message.toString()
            }
        } else {
            responseWrapper.meta.message.toString()
        }
        return ResponseHandler.OnFailed(
            HttpErrorCode.UNAUTHORIZED.code,
            message,
            responseWrapper.meta.messageCode.toString()
        )
    }

    /**
     * Response parsing for 422 status code
     * */
    private fun parseServerSideErrorResponse(response: ResponseBody?): ResponseHandler.OnFailed {
        val message: String
        val bodyString = response?.string()
        val responseWrapper: ErrorWrapper = JSON.readValue(bodyString!!)

        message = if (responseWrapper.meta!!.statusCode == 422) {
            if (responseWrapper.errors != null) {
                HttpCommonMethod.getErrorMessage(responseWrapper.errors)
            } else {
                responseWrapper.meta.message.toString()
            }
        } else {
            responseWrapper.meta.message.toString()
        }
        return ResponseHandler.OnFailed(
            HttpErrorCode.EMPTY_RESPONSE.code,
            message,
            responseWrapper.meta.messageCode.toString()
        )
    }

    /**
     * Response parsing for unknown status code
     * */
    private fun parseUnKnownStatusCodeResponse(response: ResponseBody?): ResponseHandler.OnFailed {
        val bodyString = response?.string()
        val responseWrapper: ErrorWrapper = JSON.readValue(bodyString!!)
        val message = if (responseWrapper.meta!!.statusCode == 422) {
            if (responseWrapper.errors != null) {
                HttpCommonMethod.getErrorMessage(responseWrapper.errors)
            } else {
                responseWrapper.meta.message.toString()
            }
        } else {
            responseWrapper.meta.message.toString()
        }
        return if (message.isEmpty()) {
            ResponseHandler.OnFailed(
                HttpErrorCode.EMPTY_RESPONSE.code,
                message,
                responseWrapper.meta.messageCode.toString()
            )
        } else {
            ResponseHandler.OnFailed(
                HttpErrorCode.NOT_DEFINED.code,
                message,
                responseWrapper.meta.messageCode.toString()
            )
        }
    }
}
