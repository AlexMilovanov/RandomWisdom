package com.alexmilovanov.randomwisdom.util

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.alexmilovanov.randomwisdom.R


/**
 * Extension functions for Activities
 */

fun <T> AppCompatActivity.startNewActivity(activity: Class<T>) {
    startActivity(Intent(this, activity))
    overridePendingTransitionEnter()
}

fun AppCompatActivity.startNewActivity(intent: Intent) {
    startActivity(intent)
    overridePendingTransitionEnter()
}

fun AppCompatActivity.startForResult(intent: Intent, reqCode: Int) {
    startActivityForResult(intent, reqCode)
    overridePendingTransitionEnter()
}

fun AppCompatActivity.finishWithFadeOut() {
    finish()
    overridePendingTransition(0, R.anim.activity_fade_out)
}

fun AppCompatActivity.overridePendingTransitionExit() {
    overridePendingTransition(R.anim.activity_slide_from_left, R.anim.activity_slide_to_right)
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

        setCustomAnimations(
                R.animator.fragment_slide_from_left, R.animator.fragment_slide_to_right,
                R.animator.fragment_slide_from_right, R.animator.fragment_slide_to_left
        )

        replace(frameId, fragment)
        if (addToBackStack) {
            addToBackStack(null)
        }
    }
}

/**
 * Overrides the pending Activity transition by performing the "Enter" animation.
 */
fun AppCompatActivity.overridePendingTransitionEnter() {
    overridePendingTransition(R.anim.activity_slide_from_right, R.anim.activity_slide_to_left)
}

/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transaction(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}