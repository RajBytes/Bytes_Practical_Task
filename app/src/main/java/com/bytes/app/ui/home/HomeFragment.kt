package com.bytes.app.ui.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import com.bytes.app.R
import com.bytes.app.base.FragmentBase
import com.bytes.app.base.ToolbarModel
import com.bytes.app.databinding.FragmentHomeBinding
import com.bytes.app.network.ResponseHandler
import com.bytes.app.utils.getQueryTextChangeStateFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class HomeFragment : FragmentBase<HomeViewModel, FragmentHomeBinding>(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Products", true))
    }

    override fun getViewModelClass(): Class<HomeViewModel> = HomeViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        job = Job()
        viewModel.callApi()
    }

    override fun initializeScreenVariables() {
        observeData()
        getDataBinding().viewmodel = viewModel

        getDataBinding().swipeContainer.setOnRefreshListener {
            viewModel.callApi()
        }

        launch {
            getDataBinding().svHomeData.getQueryTextChangeStateFlow()
                .debounce(300)
                .filter { query ->
                    if (query.isEmpty()) {
//                        viewModel.callApi()
                        return@filter false
                    } else return@filter query.trim().isNotEmpty()
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query)
                        .catch {
                            emitAll(flowOf(query))
                        }

                }
                .flowOn(Dispatchers.Default)
                .collect { result ->
                    viewModel.searchValue.value = result
//                    viewModel.callSearchApi()
                    Log.d("Search Query===>", "initializeScreenVariables: $result")
                }
        }

    }


    private fun observeData() {
        viewModel.responseLiveHomeVendorListResponse.observe(this, Observer {
            if (it == null) {
                return@Observer
            }
            when (it) {
                is ResponseHandler.Loading -> {
                    if (!getDataBinding().swipeContainer.isRefreshing) {
                        viewModel.showProgressBar(true)
                    }
                }
                is ResponseHandler.OnFailed -> {
                    viewModel.showProgressBar(false)
                    httpFailedHandler(it.code, it.message, it.messageCode)
                }
                is ResponseHandler.OnSuccessResponse<ProductListResponse?> -> {
                    viewModel.showProgressBar(false)
                    getDataBinding().model = it.response?.vendors
                    getDataBinding().callback = object : ProductFavoriteClickListener {
                        override fun favoriteClick(product: Product) {
                            when (product.is_product_Favourite) {
                                true -> {
                                    viewModel.callUnFavoriteApi(product)
                                }
                                false -> {
                                    viewModel.callFavoriteApi(product)
                                }
                            }
                        }
                    }
                    getDataBinding().swipeContainer.isRefreshing = false
                    viewModel.isNotifyDataChanged.value = true
                }
            }
        })

        viewModel.responseFavorite.observe(this, Observer {
            if (it == null) {
                return@Observer
            }
            when (it) {
                is ResponseHandler.Loading -> {
                    if (!getDataBinding().swipeContainer.isRefreshing) {
                        viewModel.showProgressBar(true)
                    }
                }
                is ResponseHandler.OnFailed -> {
                    viewModel.showProgressBar(false)
                    httpFailedHandler(it.code, it.message, it.messageCode)
                }
                is ResponseHandler.OnSuccessResponse<FevoriteResponse?> -> {
                    viewModel.showProgressBar(false)
                    viewModel.realProduct?.is_product_Favourite = true
                    getDataBinding().rvHomeVendor.adapter?.notifyDataSetChanged()
                }
            }
        })

        viewModel.responseUnFavorite.observe(this, Observer {
            if (it == null) {
                return@Observer
            }
            when (it) {
                is ResponseHandler.Loading -> {
                    if (!getDataBinding().swipeContainer.isRefreshing) {
                        viewModel.showProgressBar(true)
                    }

                    getDataBinding().rvHomeVendor.adapter?.notifyDataSetChanged()
                }
                is ResponseHandler.OnFailed -> {
                    viewModel.showProgressBar(false)
                    httpFailedHandler(it.code, it.message, it.messageCode)
                }
                is ResponseHandler.OnSuccessResponse<FevoriteResponse?> -> {
                    viewModel.showProgressBar(false)
                    viewModel.realProduct?.is_product_Favourite = false
                    getDataBinding().rvHomeVendor.adapter?.notifyDataSetChanged()
                }
            }
        })
    }

    private fun dataFromNetwork(query: String): Flow<String> {
        return flow {
            emit(query)
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }
}