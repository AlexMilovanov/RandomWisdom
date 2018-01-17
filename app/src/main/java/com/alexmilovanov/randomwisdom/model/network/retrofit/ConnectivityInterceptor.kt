package com.alexmilovanov.randomwisdom.model.network.retrofit

import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.errorhandling.NoNetworkException
import com.alexmilovanov.randomwisdom.view.util.ResourceProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject


// Class that implements Interceptor to perform a network connectivity check before executing the request.
class ConnectivityInterceptor @Inject constructor(private val connectManager: ConnectivityManager,
                                                  private val resProvider: ResourceProvider)
    : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        if(!isOnline()){
            throw NoNetworkException(resProvider.getString(R.string.error_message_no_network))
        }

        val original = chain.request()
        // Customize the request
        return chain.proceed(original.newBuilder().build())
    }

    // Check whether network is currently available
    private fun isOnline(): Boolean {
        val netInfo: NetworkInfo? = connectManager.activeNetworkInfo;
        return (netInfo != null && netInfo.isConnected)
    }

}