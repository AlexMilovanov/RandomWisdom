package com.alexmilovanov.randomwisdom.util

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity


/**
 * Extension functions for Activities
 */

fun <T> AppCompatActivity.startNewActivity(activity: Class<T>) {
    startActivity(Intent(this, activity))
}

fun AppCompatActivity.startNewActivity(intent: Intent) {
    startActivity(intent)
}

/**
 * Fragment is added to the container view with id "frameId". The operation is
 * performed by the FragmentManager.
 */
fun AppCompatActivity.replaceFragmentInActivity(
        fragment: Fragment,
        frameId: Int,
        addToBackStack: Boolean = false) {

    supportFragmentManager.transaction {
        replace(frameId, fragment)
        if (addToBackStack) {
            addToBackStack(null)
        }
    }
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transaction(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}