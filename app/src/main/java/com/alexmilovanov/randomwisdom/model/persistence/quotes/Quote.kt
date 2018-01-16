package com.alexmilovanov.randomwisdom.model.persistence.quotes

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Represents a product data class that holds a database row.
 * For each entity, a database table is created to hold the items.
 */
@Entity(tableName = "favorite_quotes")
data class Quote (@PrimaryKey
                  @ColumnInfo(name = "id")
                  var id: Long,

                  @ColumnInfo(name = "text")
                  var text: String,

                  @ColumnInfo(name = "author")
                  var author: String
)