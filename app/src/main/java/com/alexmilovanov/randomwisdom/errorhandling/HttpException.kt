package com.alexmilovanov.randomwisdom.errorhandling


/**
 * Exception thrown when any http error occurred
 */
class HttpException (override val message: String) : RuntimeException(message)