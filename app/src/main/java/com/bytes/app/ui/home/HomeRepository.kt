package com.bytes.app.ui.home

import com.bytes.app.base.BaseRepository
import com.bytes.app.network.ApiInterface
import com.bytes.app.network.ResponseHandler
import javax.inject.Inject

/**
 * This is the actual Implementation of the Home Repository where we are making an api call.
 */
class HomeRepository @Inject constructor(private val apiInterface: ApiInterface) :
    BaseRepository() {

    suspend fun callProductListApi(): ResponseHandler<ProductListResponse?> {
        return makeAPICall {
            apiInterface.callProductListApi()
        }
    }

    suspend fun callFavoriteApi(): ResponseHandler<FevoriteResponse?> {
        return makeAPICall {
            apiInterface.callFavoritesApi()
        }
    }

    suspend fun callUnFavoriteApi(): ResponseHandler<FevoriteResponse?> {
        return makeAPICall {
            apiInterface.callUnFavoritesApi()
        }
    }


}