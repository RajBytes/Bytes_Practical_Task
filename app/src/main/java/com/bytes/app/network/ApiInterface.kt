package com.bytes.app.network

import com.bytes.app.ui.home.FevoriteResponse
import com.bytes.app.ui.home.ProductListResponse
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Singleton

/**
 * Interface used for make an api
 */
@Singleton
interface ApiInterface {
    @GET("products")
    suspend fun callProductListApi(): Response<ProductListResponse>

    @POST("favorites")
    suspend fun callFavoritesApi(): Response<FevoriteResponse>

    @DELETE("favorites")
    suspend fun callUnFavoritesApi(): Response<FevoriteResponse>
}
