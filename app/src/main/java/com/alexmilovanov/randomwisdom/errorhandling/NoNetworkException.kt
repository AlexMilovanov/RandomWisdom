package com.alexmilovanov.randomwisdom.errorhandling


/**
 * Exception thrown when no available network is found
 */
class NoNetworkException (override val message: String) : RuntimeException(message)