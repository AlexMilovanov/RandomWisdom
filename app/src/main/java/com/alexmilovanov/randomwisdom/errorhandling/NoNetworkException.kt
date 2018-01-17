package com.alexmilovanov.randomwisdom.errorhandling

import java.io.IOException


// Exception thrown when no available network is found
class NoNetworkException (override val message: String) : IOException() {

    override fun getLocalizedMessage(): String = message

}