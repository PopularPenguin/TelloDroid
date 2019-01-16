package com.popularpenguin.tellodroid.controller

import android.view.MotionEvent
import com.popularpenguin.tellodroid.tello.Tello

class XboxController(private val tello: Tello) : Controller {
    companion object {
        // D-Pad
        const val UP = 19
        const val DOWN = 20
        const val LEFT = 21
        const val RIGHT = 22

        const val A = 96
        const val B = 97
        const val X = 98
        const val Y = 99

        const val LB = 100
        const val RB = 101

        const val BACK = 102
        const val START = 103
    }

    override fun processAnalog(event: MotionEvent) {
        // TODO: Return the command string from a when block
        // TODO: Need to spawn coroutines so I can poll multiple sticks and triggers at once?

        // Left Stick
        when (event.x) {
            // ...
        }
        when (event.y) {
            //in 0.5f..1.0f -> up(event.y) // TODO: Check to ensure axis isn't inverted
           // in -0.5f..-1.0f -> down(event.y) // TODO: Check to ensure axis isn't inverted
        }

        // TODO: Remove this if block
        if (event.getAxisValue(MotionEvent.AXIS_Z) == 1.0f) {

        }
    }

    override fun processDigital(keyCode: Int) {
        // TODO: Return the command string from a when block
       when (keyCode) {
           A -> tello.land()
           Y -> tello.takeOff()
           X -> tello.rotate(-45) // TODO: Placeholder, move to left d-pad after testing
           B -> tello.rotate(45) // TODO: Placeholder, move to left d-pad after testing

           // ...
        }
    }
}