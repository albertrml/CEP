package br.com.arml.cep.di

import br.com.arml.cep.ui.screen.cache.CacheReducer
import br.com.arml.cep.ui.screen.cache.CacheState
import br.com.arml.cep.ui.screen.favorite.FavoriteReducer
import br.com.arml.cep.ui.screen.favorite.FavoriteState
import br.com.arml.cep.ui.screen.log.LogReducer
import br.com.arml.cep.ui.screen.log.LogState
import br.com.arml.cep.ui.screen.search.SearchReducer
import br.com.arml.cep.ui.screen.search.SearchState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {
    @Provides
    fun provideCacheState(): CacheState = CacheState()

    @Provides
    fun provideCacheReducer(): CacheReducer = CacheReducer()

    @Provides
    fun provideFavoriteState(): FavoriteState = FavoriteState()

    @Provides
    fun provideFavoriteReducer(): FavoriteReducer = FavoriteReducer()

    @Provides
    fun provideLogState(): LogState = LogState()

    @Provides
    fun provideLogReducer(): LogReducer = LogReducer()

    @Provides
    fun provideSearchState(): SearchState = SearchState()

    @Provides
    fun provideSearchReducer(): SearchReducer = SearchReducer()
}