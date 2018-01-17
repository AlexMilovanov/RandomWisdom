package com.alexmilovanov.randomwisdom.di.data

import android.arch.persistence.room.Room
import android.content.Context
import com.alexmilovanov.randomwisdom.di.ApplicationContext
import com.alexmilovanov.randomwisdom.data.persistence.AppDatabase
import com.alexmilovanov.randomwisdom.data.persistence.quotes.FavoritesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Module providing persistence related dependencies
 */
@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
            Room.databaseBuilder(ctx, AppDatabase::class.java, "randomwisdom.db").build()

    @Provides
    @Singleton
    fun provideFavoritesDao(db: AppDatabase): FavoritesDao = db.favoritesDao()

}
