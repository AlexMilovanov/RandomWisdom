package com.alexmilovanov.randomwisdom.util

import timber.log.Timber

/**
 * Log a message with default settings.
 */
fun log(msg: String) = logV(msg, true)

/**
 * Log a verbose message.
 */
fun logV(msg: String, showThread: Boolean = false) = Timber.v(getFullMessage(msg, showThread))

/**
 * Log a debug message.
 */
fun logD(msg: String, showThread: Boolean = false) = Timber.d(getFullMessage(msg, showThread))

/**
 * Log an info message.
 */
fun logI(msg: String, showThread: Boolean = false) =  Timber.i(getFullMessage(msg, showThread))

/**
 * Log a warning message.
 */
fun logW(msg: String, showThread: Boolean = false) = Timber.i(getFullMessage(msg, showThread))

/**
 * Log an error message.
 */
fun logE(msg: String, showThread: Boolean = false) = Timber.e(getFullMessage(msg, showThread))

/**
 * Append current thread name to provided message if required
 */
private fun getFullMessage(origMsg: String, appendThread: Boolean) =
        if (appendThread)
            "[${Thread.currentThread().name}] $origMsg"
        else
            origMsg
