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

    private val degrees = 45
    private val distance = 40

    override fun processAnalog(event: MotionEvent) {
        // Left Stick
        when (event.x) {
            in -1.0f..-0.9f -> tello.rotate(-degrees)
            in 0.9f..1.0f -> tello.rotate(degrees)
        }
        when (event.y) {
            in -1.0f..-0.9f -> tello.up(distance)
            in 0.9f..1.0f -> tello.down(distance)
        }

        // Right stick
        when (event.getAxisValue(MotionEvent.AXIS_RX)) {
            in -1.0f..-0.9f -> tello.left(distance)
            in 0.9f..1.0f -> tello.right(distance)
        }
        when (event.getAxisValue(MotionEvent.AXIS_RY)) {
            in -1.0f..-0.9f -> tello.forward(distance)
            in 0.9f..1.0f -> tello.back(distance)
        }

        // Left trigger
        if (event.getAxisValue(MotionEvent.AXIS_Z) == 1.0f) {
            // TODO: Assign button
        }

        // Right trigger
        if (event.getAxisValue(MotionEvent.AXIS_RZ) == 1.0f) {
            // TODO: Assign button
        }

    }

    override fun processDigital(keyCode: Int) {
       when (keyCode) {
           A -> tello.land()
           Y -> tello.takeOff()
           B -> tello.connect()

           // ...
        }
    }
}