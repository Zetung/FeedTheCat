package com.zetung.android.feedthecat

import androidx.fragment.app.Fragment

fun Fragment.coordinator(): CoordInterface {
    return requireActivity() as CoordInterface
}

interface CoordInterface{
    fun start()
    fun finish()
    fun startEnterFragment()
    fun startMainFragment()
    fun startResultsFragment()
}