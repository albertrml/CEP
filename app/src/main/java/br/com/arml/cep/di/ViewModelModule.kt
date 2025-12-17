package br.com.arml.cep.di

import br.com.arml.cep.ui.screen.log.LogReducer
import br.com.arml.cep.ui.screen.log.LogState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideLogState(): LogState = LogState()

    @Provides
    fun provideLogReducer(): LogReducer = LogReducer()

}