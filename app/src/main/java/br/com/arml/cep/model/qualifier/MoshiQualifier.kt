package br.com.arml.cep.model.qualifier

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitMoshi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BackupMoshi