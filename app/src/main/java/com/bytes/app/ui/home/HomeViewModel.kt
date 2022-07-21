package com.bytes.app.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bytes.app.base.ViewModelBase
import com.bytes.app.network.ResponseHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This is the ViewModel class for Home Screen.
 * We used the injection by Hilt which will manage for the Home View  Model.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(private val homeRepository: HomeRepository) :
    ViewModelBase() {

    var isNotifyDataChanged = MutableLiveData<Boolean>(false)

    var responseLiveHomeVendorListResponse =
        MutableLiveData<ResponseHandler<ProductListResponse?>>()

    var responseFavorite =
        MutableLiveData<ResponseHandler<FevoriteResponse?>>()

    var responseUnFavorite =
        MutableLiveData<ResponseHandler<FevoriteResponse?>>()

    var searchValue = MutableLiveData<String>("")
    var realProduct: Product? = null


    /**
     * Here we are calling the Product List api in ViewModel scope.
     */
    fun callApi() {
        viewModelScope.launch {
            responseLiveHomeVendorListResponse.postValue(ResponseHandler.Loading)
            responseLiveHomeVendorListResponse.value = homeRepository.callProductListApi()
        }
    }

    /**
     * Here we are calling the Favorite Product api in ViewModel scope.
     */
    fun callFavoriteApi(product: Product) {
        realProduct = product
        viewModelScope.launch {
            responseFavorite.value = homeRepository.callFavoriteApi()
        }
    }

    /**
     * Here we are calling the Un-Favorite Product api in ViewModel scope.
     */
    fun callUnFavoriteApi(product: Product) {
        realProduct = product
        viewModelScope.launch {
            responseUnFavorite.value = homeRepository.callUnFavoriteApi()

        }
    }

}