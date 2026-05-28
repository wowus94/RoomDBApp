@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package ru.shevrus.roomdbapp.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

@Database(
    entities = [ProductEntity::class, FavoriteEntity::class],
    version = 2
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): ProductDao
}


val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "CREATE TABLE IF NOT EXISTS `favorite_products` (`id` INTEGER NOT NULL, PRIMARY KEY(`id`))"
        )
    }
}
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor: RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}