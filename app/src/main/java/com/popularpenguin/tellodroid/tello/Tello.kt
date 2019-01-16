package com.popularpenguin.tellodroid.tello

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.TextView
import com.popularpenguin.tellodroid.controller.XboxController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Class acting as a view model
 */
class Tello {

    // TODO: Move individual commands to this class
    private val command = TelloCommand()
    private val state = TelloState()
    // TODO: Add video stream here

    private val controller = XboxController(this)

    private var log = mutableListOf<String>()
    private lateinit var logView: TextView

    fun connect() {
        GlobalScope.launch(Dispatchers.IO) {
            command.connect()
            //state.connect()

            log.add("Connecting\n")
        }
    }

    private fun sendCommand(cmd: String) {
        GlobalScope.launch(Dispatchers.Main) {
            command.sendCommand(cmd)

            log.add("$cmd\n")
            printLog()
        }
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent) {
        if (event.source and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD) {
            controller.processDigital(keyCode)
        }
    }

    fun onKeyUp(keyCode: Int, event: KeyEvent) {
        // TODO: Probably don't have to do anything in this function
    }

    fun onMotionEvent(event: MotionEvent) {
        controller.processAnalog(event)
    }

    fun takeOff() { sendCommand("takeoff") }

    fun land() { sendCommand("land") }

    // Stop motors immediately
    fun emergency() { sendCommand("emergency") }

    // Fly up 20-500 cm
    fun up(distance: Int) {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        sendCommand("up $distance")
    }

    // Fly down 20-500 cm
    fun down(distance: Int) {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        sendCommand("down $distance")
    }

    // Fly forward 20-500 cm
    fun forward(distance: Int) {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        sendCommand("forward $distance")
    }

    // Fly back 20-500 cm
    fun back(distance: Int) {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        sendCommand("back $distance")
    }

    // Fly left 20-500 cm
    fun left(distance: Int) {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        sendCommand("left $distance")
    }

    // Fly right 20-500 cm
    fun right(distance: Int) {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        sendCommand("right $distance")
    }

    fun rotate(degrees: Int) {
        when (degrees) {
            in -3600..-1 -> {
                sendCommand("ccw ${abs(degrees)}")
            }
            0 -> return
            in 1..3600 -> {
                sendCommand("cw $degrees")
            }
            else -> IllegalArgumentException("Invalid degrees: Must be in range -3600..3600")
        }
    }

    fun flip(direction: String) {
        when (direction) {
            "l", "r", "f", "b" -> sendCommand("flip $direction")
            else -> java.lang.IllegalArgumentException("Invalid direction: Must be l, r, f, b")
        }
    }

    // TODO: Put in the rest of the commands


    fun close() {
        command.close()
        state.close()
    }

    private fun printLog() {
        logView.text = ""

        if (log.size <= 5) {
            log.forEach {
                logView.append(it)
            }
        }
        else {
            for (i in 1..5) {
                logView.append(log[log.size - i])
            }
        }
    }
    fun subscribeToLog(view: TextView) {
        logView = view
    }
}