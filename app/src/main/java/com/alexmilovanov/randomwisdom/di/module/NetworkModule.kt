package com.alexmilovanov.randomwisdom.di.module

import android.content.Context
import android.net.ConnectivityManager
import com.alexmilovanov.randomwisdom.di.ApplicationContext
import com.alexmilovanov.randomwisdom.data.ApiConstants.Companion.BASE_URL
import com.alexmilovanov.randomwisdom.data.ApiConstants.Companion.HTTP_CONNECTION_TIMEOUT_SECONDS
import com.alexmilovanov.randomwisdom.data.network.ApiService
import com.alexmilovanov.randomwisdom.data.network.retrofit.ConnectivityInterceptor
import com.alexmilovanov.randomwisdom.data.network.retrofit.ErrorInterceptor
import com.alexmilovanov.randomwisdom.data.network.retrofit.HeadersInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
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
    internal fun provideOkHttpClient(connectivityInterceptor: ConnectivityInterceptor,
                                     errorInterceptor: ErrorInterceptor,
                                     headersInterceptor: HeadersInterceptor): OkHttpClient {
        // The singleton HTTP client.
        val builder = OkHttpClient.Builder().apply {
            // Define timeout values
            connectTimeout(HTTP_CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            readTimeout(HTTP_CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            writeTimeout(HTTP_CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            // Add required interceptors
            addInterceptor(connectivityInterceptor)
            addInterceptor(errorInterceptor)
            addInterceptor(headersInterceptor)
            // Enable logs only in debug version
            addInterceptor(HttpLoggingInterceptor().setLevel(
                    HttpLoggingInterceptor.Level.BODY
                    // if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    // else HttpLoggingInterceptor.Level.NONE)
            ))
        }
        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideApiService(client: OkHttpClient): ApiService {

        val builder = Retrofit.Builder().apply {
            baseUrl(BASE_URL)
            addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            client(client)
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