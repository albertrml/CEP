package br.com.arml.cep.model.qualifier

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitMoshi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BackupMoshi