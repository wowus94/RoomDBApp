package ru.shevrus.roomdbapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_products")
data class FavoriteEntity(
    @PrimaryKey val id: Long
)