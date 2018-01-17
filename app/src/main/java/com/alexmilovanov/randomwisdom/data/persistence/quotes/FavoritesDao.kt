package com.alexmilovanov.randomwisdom.data.persistence.quotes

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Maybe

/**
 * Interface for database access on favorite quotes related operations.
 */
@Dao
abstract class FavoritesDao {

    @Query("SELECT * FROM favorites ORDER BY id")
    abstract fun loadFavoriteQuotes(): Maybe<List<Quote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addToFavorites(quote: Quote)

    @Query("DELETE FROM favorites WHERE favorites.id = :id")
    abstract fun deleteFromFavorites(id: Long)

    @Query("DELETE FROM favorites")
    abstract fun clearAll()

}