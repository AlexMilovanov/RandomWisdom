package com.alexmilovanov.randomwisdom.di.data

import android.content.Context
import android.net.ConnectivityManager
import com.alexmilovanov.randomwisdom.BuildConfig
import com.alexmilovanov.randomwisdom.di.ApplicationContext
import com.alexmilovanov.randomwisdom.model.ApiConstants.Companion.BASE_URL
import com.alexmilovanov.randomwisdom.model.ApiConstants.Companion.HTTP_CONNECTION_TIMEOUT_SECONDS
import com.alexmilovanov.randomwisdom.model.network.ApiService
import com.alexmilovanov.randomwisdom.model.network.retrofit.ConnectivityInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Module providing REST API related dependencies
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    internal fun provideOkHttpClient(networkInterceptor: ConnectivityInterceptor): OkHttpClient {
        // The singleton HTTP client.
        val builder = OkHttpClient.Builder().apply {
            // Define timeout values
            connectTimeout(HTTP_CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            readTimeout(HTTP_CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            writeTimeout(HTTP_CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            // Add required interceptors
            addInterceptor(networkInterceptor)
            // Enable logs only in debug version
            addInterceptor(HttpLoggingInterceptor().setLevel(
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE)
            )
        }
        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideApiService(client: OkHttpClient): ApiService {

        val builder = Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            client(client)
            //addCallAdapterFactory(LiveDataCallAdapterFactory())
            addConverterFactory(GsonConverterFactory.create(buildGson()))
        }

        return builder.build().create(ApiService::class.java)
    }

    @Provides
    @Singleton
    internal fun provideConnectionManager(@ApplicationContext ctx: Context): ConnectivityManager {
        return ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private fun buildGson(): Gson {
        val builder = GsonBuilder().apply {
            setLenient()
        }
        return builder.create()
    }
}