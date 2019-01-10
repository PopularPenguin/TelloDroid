package com.popularpenguin.tellodroid.controller

import android.view.MotionEvent

class XboxController : Controller {
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

    /**
     *
     * @event
     * @return Tello command
     */
    override fun processAnalog(event: MotionEvent): String {
        // TODO: Return the command string from a when block
        // TODO: Need to spawn coroutines so I can poll multiple sticks and triggers at once?

        // Left Stick
        when (event.x) {
            // ...
        }
        when (event.y) {
            in 0.5f..1.0f -> up(event.y) // TODO: Check to ensure axis isn't inverted
            in -0.5f..-1.0f -> down(event.y) // TODO: Check to ensure axis isn't inverted
        }

        // TODO: Remove this if block
        if (event.getAxisValue(MotionEvent.AXIS_Z) == 1.0f) {
            return "takeoff"
        }


        return ""
    }

    override fun processDigital(keyCode: Int): String {
        // TODO: Return the command string from a when block
       return when (keyCode) {
           A -> "land" // TODO: Change
           Y -> "takeoff"

           // ...
           else -> "NA"
        }
    }

    // TODO: Possibly process commands here?
    // TODO: Implement an interface for these...
    // 20 - 500
    private fun up(value: Float): String {
        // TODO: Process the command and return the correct command as a string
        return ""
    }

    private fun down(value: Float): String {
        return ""
    }
}