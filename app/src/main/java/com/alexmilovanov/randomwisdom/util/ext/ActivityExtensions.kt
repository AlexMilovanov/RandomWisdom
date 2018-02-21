package com.alexmilovanov.randomwisdom.util.ext

import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alexmilovanov.randomwisdom.R
import com.alexmilovanov.randomwisdom.util.resources.IResourceProvider
import io.reactivex.Maybe


/**
 * Extension functions for AppCompatActivity class
 */

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

fun AppCompatActivity.finishWithSlideAway() {
    finish()
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
 * Display a Snackbar. Action button click Listener and background color are optional.
 */
fun AppCompatActivity.showSimpleSnackbar(layout: View,
                                   resProvider: IResourceProvider,
                                   msg: String,
                                   length: Int = Snackbar.LENGTH_LONG,
                                   isError: Boolean = true) {
    val snackbar = Snackbar.make(layout, msg, length)
    snackbar.view.apply {
        setBackgroundColor(resProvider.color(if (isError) R.color.colorRed else R.color.colorGray))
    }
    snackbar.show()
}

/**
 * Allows to subscribe to updates when the action is performed, and when the Snackbar is dismissed
 */
fun AppCompatActivity.showActionSnackbar(layout: View,
                                         resProvider: IResourceProvider,
                                         msg: String,
                                         length: Int = Snackbar.LENGTH_INDEFINITE,
                                         isError: Boolean = true,
                                         actionTitle: String = ""): Maybe<Boolean> {

    // "using" operator allows to bind the lifetime of a resource to the lifetime of an observable sequence.
    // In this case, we only want the Snackbar to exist while the Subscriber is subscribed.
    return Maybe.using(
            {
                // The resource supplier is just a Callable that provides the resource
                // to be used within the sequence.
                Snackbar.make(layout, msg, length)
            },
            { // Creating the actual MaybeSource to subscribe to
                snackbar ->
                // Inside the create method is where we use the Snackbar by providing
                // anonymous callbacks for click and dismiss events.
                // Inside these callbacks we update the Subscriber with the relevant method.
                Maybe.create<Boolean> { emitter ->
                    snackbar.setAction(actionTitle, {
                        // onSuccess signals that the user has clicked on the button
                        if (!emitter.isDisposed)
                            emitter.onSuccess(true)
                    })

                    snackbar.addCallback(object : Snackbar.Callback() {
                        @Override
                        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                            // onComplete signals that the Snackbar has been dismissed without the user clicking the button.
                            if (!emitter.isDisposed)
                                emitter.onComplete()
                        }
                    })

                    snackbar.view.apply {
                        setBackgroundColor(resProvider.color(if (isError) R.color.colorRed else R.color.colorGray))
                    }
                    snackbar.show()
                }
            },
            { // Creating a new Consumer that disposes the resource created in the Callable in the first parameter.
                sb ->
                if (sb.isShown)
                    sb.dismiss()
            }
    )
}


/**
 * Runs a FragmentTransaction, then calls commit().
 */
private inline fun FragmentManager.transaction(action: FragmentTransaction.() -> Unit) {
    beginTransaction().apply {
        action()
    }.commit()
}