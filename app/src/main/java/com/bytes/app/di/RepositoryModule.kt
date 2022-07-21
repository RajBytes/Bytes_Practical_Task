package com.bytes.app.di

import com.bytes.app.network.ApiInterface
import com.bytes.app.ui.home.HomeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

/**
 * This is the Repo Module which we initialized using Hilt Depedency Injection.
 * Now When we need an Access of the Home Repo we just need to use the Inject annotation everything else
 * will be taken care by Hilt.
 */
@Module
@InstallIn(ViewModelComponent::class)
class RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideHomeRepository(apiInterface: ApiInterface) = HomeRepository(apiInterface)
}