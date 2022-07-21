package com.bytes.app.ui.splash

import android.os.Bundle
import androidx.navigation.findNavController
import com.bytes.app.R
import com.bytes.app.base.FragmentBase
import com.bytes.app.base.ToolbarModel
import com.bytes.app.base.ViewModelBase
import com.bytes.app.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : FragmentBase<ViewModelBase, FragmentSplashBinding>() {


    override fun getLayoutId(): Int {
        return R.layout.fragment_splash
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(false, null, false))
    }

    override fun getViewModelClass(): Class<ViewModelBase> = ViewModelBase::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initializeScreenVariables() {

    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(context = Dispatchers.Main) {
            delay(3000)
            val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            getDataBinding().main.findNavController().navigate(action)
        }
    }

}
