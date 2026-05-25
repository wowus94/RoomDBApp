package ru.shevrus.roomdbapp.domain.model

sealed class AppError : Exception() {
    sealed class Network : AppError() {
        object NotFound : Network()
        object ServerError : Network()
        object NoInternet : Network()
        object Unknown : Network()
    }
    sealed class Database : AppError() {
        object DiskFull : Database()
        object Unknown : Database()
    }
    object Unknown : AppError()
}