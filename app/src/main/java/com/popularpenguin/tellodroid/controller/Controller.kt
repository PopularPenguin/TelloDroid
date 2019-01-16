package com.popularpenguin.tellodroid.controller

import android.view.MotionEvent

interface Controller {
    fun processAnalog(event: MotionEvent)
    fun processDigital(keyCode: Int)
}