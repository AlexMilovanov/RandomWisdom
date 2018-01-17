package com.alexmilovanov.randomwisdom.data.persistence

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.alexmilovanov.randomwisdom.data.persistence.quotes.FavoritesDao
import com.alexmilovanov.randomwisdom.data.persistence.quotes.Quote


/**
 * Component to create a database holder. The annotation defines the list of entities,
 * and the class's content defines the list of data access objects (DAOs) in the database.
 */
@Database(entities = [Quote::class],
          version = 1,
          exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

}