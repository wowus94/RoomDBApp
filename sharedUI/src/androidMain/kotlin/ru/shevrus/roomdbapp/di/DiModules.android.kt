package ru.shevrus.roomdbapp.di

import org.koin.core.module.Module
import org.koin.dsl.module
import ru.shevrus.roomdbapp.data.database.getDatabaseBuilder

actual val platformModule: Module = module {

    single { getDatabaseBuilder() }
}