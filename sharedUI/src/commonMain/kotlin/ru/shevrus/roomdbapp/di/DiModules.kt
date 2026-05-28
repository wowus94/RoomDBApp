package ru.shevrus.roomdbapp.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.shevrus.roomdbapp.data.database.AppDatabase
import ru.shevrus.roomdbapp.data.database.getDatabaseBuilder
import ru.shevrus.roomdbapp.data.database.getRoomDatabase
import ru.shevrus.roomdbapp.data.network.ProductApi
import ru.shevrus.roomdbapp.data.repository.ProductRepositoryImpl
import ru.shevrus.roomdbapp.domain.repository.ProductRepository
import ru.shevrus.roomdbapp.domain.usecase.GetProductsUseCase
import ru.shevrus.roomdbapp.domain.usecase.SyncProductsUseCase
import ru.shevrus.roomdbapp.presentation.ProductViewModel
import ru.shevrus.roomdbapp.presentation.util.UiErrorTranslator

val commonModule = module {

    factory { GetProductsUseCase(get()) }
    factory { SyncProductsUseCase(get()) }

    factoryOf(::ProductViewModel)
    factoryOf(::UiErrorTranslator)

}

val databaseModule = module {

    single { getDatabaseBuilder() }
    single { getRoomDatabase(get()) }
    single { ProductApi() }
    single { ProductRepositoryImpl(get(), get()) } bind ProductRepository::class

    single { get<AppDatabase>().getDao() }
}

fun initKoin(additionalModules: List<Module> = emptyList()) {
    startKoin {
        modules(
            commonModule,
            databaseModule,
            platformModule,
            *additionalModules.toTypedArray()
        )
    }
}

expect val platformModule: Module