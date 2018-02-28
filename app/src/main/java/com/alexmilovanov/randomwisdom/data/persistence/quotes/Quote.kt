package com.alexmilovanov.randomwisdom.data.persistence.quotes

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Represents a product data class that holds a database row.
 * For each entity, a database table is created to hold the items.
 */
@Entity(tableName = "favorites")
data class Quote (
        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        var id: String = UUID.randomUUID().toString(),

        @ColumnInfo(name = "quote")
        var quote: String = "",

        @ColumnInfo(name = "author")
        var author: String = "",

        @ColumnInfo(name = "timestamp")
        var timestamp: Long = 0,

        @Ignore
        var isLiked: Boolean = false
)