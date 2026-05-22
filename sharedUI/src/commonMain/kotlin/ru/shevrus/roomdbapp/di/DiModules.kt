package ru.shevrus.roomdbapp.di

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.shevrus.roomdbapp.data.database.AppDatabase
import ru.shevrus.roomdbapp.data.database.getDatabaseBuilder
import ru.shevrus.roomdbapp.data.database.getRoomDatabase
import ru.shevrus.roomdbapp.data.repository.TodoRepositoryImpl
import ru.shevrus.roomdbapp.domain.repository.TodoRepository
import ru.shevrus.roomdbapp.domain.usecase.ClearTableUseCase
import ru.shevrus.roomdbapp.domain.usecase.GetTableUseCase
import ru.shevrus.roomdbapp.domain.usecase.InsertTableUseCase
import ru.shevrus.roomdbapp.presentation.TodoViewModel

val commonModule = module {

    single { GetTableUseCase(get()) }
    single { InsertTableUseCase(get()) }
    single { ClearTableUseCase(get()) }

    factoryOf(::TodoViewModel)

}

val databaseModule = module {

    single { getDatabaseBuilder() }
    single { getRoomDatabase(get()) }

    single { get<AppDatabase>().getDao() }
    single { TodoRepositoryImpl(get()) } bind TodoRepository::class
}

fun initKoin(additionalModules: List<Module> = emptyList()) {
    org.koin.core.context.startKoin {
        modules(
            commonModule,
            databaseModule,
            platformModule,
            *additionalModules.toTypedArray()
        )
    }
}

expect val platformModule: Module