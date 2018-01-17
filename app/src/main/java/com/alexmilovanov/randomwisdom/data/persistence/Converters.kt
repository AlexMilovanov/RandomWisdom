package com.alexmilovanov.randomwisdom.data.persistence

import android.arch.persistence.room.TypeConverter

/**
 * Specifies additional type converters that Room can use (all Daos and Entities in the database).
 */
class Converters {

    @TypeConverter
    fun toIntFromBoolean(b: Boolean) : Int = if (b) 1 else 0

    @TypeConverter
    fun toBooleanFromInt(i: Int) : Boolean = i!=0

}