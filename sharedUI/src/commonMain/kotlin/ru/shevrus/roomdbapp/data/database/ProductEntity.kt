package ru.shevrus.roomdbapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String,
    val price: Double,
    val thumbnail: String,
    val isSynced: Boolean = false
)
