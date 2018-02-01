package com.alexmilovanov.randomwisdom.data.persistence.quotes

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Represents a product data class that holds a database row.
 * For each entity, a database table is created to hold the items.
 */
@Entity(tableName = "favorites")
data class Quote (
        @ColumnInfo(name = "quote")
        var quote: String,

        @ColumnInfo(name = "author")
        var author: String
) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}