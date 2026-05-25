package ru.shevrus.roomdbapp.presentation.util

import ru.shevrus.roomdbapp.domain.model.AppError

class UiErrorTranslator {
    fun translate(error: Throwable?): String {
        if (error == null) return ""

        return when (error) {
            is AppError.Network.NotFound -> "Запрашиваемые данные не найдены на сервере."
            is AppError.Network.ServerError -> "Сервер временно недоступен. Попробуйте позже."
            is AppError.Network.NoInternet -> "Не удалось подключиться к серверу. Проверьте интернет-соединение."
            is AppError.Network.Unknown -> "Произошла ошибка при передаче данных по сети."
            is AppError.Database.DiskFull -> "Недостаточно свободного места на устройстве."
            is AppError.Database.Unknown -> "Неизвестная ошибка базы данных"
            else -> "Произошла непредвиденная ошибка приложения."
        }
    }
}