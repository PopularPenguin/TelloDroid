package com.popularpenguin.tellodroid.controller

import android.view.MotionEvent

interface Controller {
    fun processAnalog(event: MotionEvent): String
    fun processDigital(keyCode: Int): String
}