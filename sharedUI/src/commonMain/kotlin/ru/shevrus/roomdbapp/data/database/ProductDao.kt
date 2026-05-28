package ru.shevrus.roomdbapp.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProductsAsFlow(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    // Favorites
    @Query("SELECT id FROM favorite_products")
    fun getFavoriteIdsFlow(): Flow<List<Long>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite_products WHERE id = :id")
    suspend fun deleteFavorite(id: Long)
}