package com.popularpenguin.tellodroid.tello

import android.util.Log
import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.TextView
import com.popularpenguin.tellodroid.controller.XboxController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketException
import kotlin.math.abs

/**
 * Class acting as a view model
 */
class Tello {

    private val TAG = "TELLO_LOG"
    // TODO: Move individual commands to this class
    private val command = TelloCommand()
    private val state = TelloState()
    @Volatile private var isReady = false
    // TODO: Add video stream here

    private val controller = XboxController(this)

    private var batteryState = ""
    private var speedState = ""
    private var timeState = ""
    private lateinit var stateView: TextView

    private var log = mutableListOf<String>()
    private lateinit var logView: TextView

    fun connect() { // TODO: Fix crash on first launch
        GlobalScope.launch(Dispatchers.IO) {
            command.connect()
            //state.connect()

            log.add("Connecting\n")

            isReady = true
        }
    }

    private fun sendCommand(cmd: String) {
        if (!isReady) {
            return
        }

        isReady = false

        GlobalScope.launch(Dispatchers.Main) {
            try {
                command.sendCommand(cmd)
            } catch (e : IOException) {
                Log.e(TAG, e.toString())
            }

            log.add("$cmd\n")
            printLog()

            isReady = true
        }
    }

    // TODO: Add a view and test
    private fun getState(cmd: String) {
        GlobalScope.launch(Dispatchers.Main) {
            while(command.isConnected()) {
                batteryState = command.sendCommand("battery?")
                speedState = command.sendCommand("speed?")
                timeState = command.sendCommand("time?")
                stateView.text = "Battery: $batteryState, Speed: $speedState, Time: $timeState"

                delay(2000L)
            }
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

    // Control commands /////////////////////////////////////////////////////////////////////
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

        logView.text = log.last()
    }

    fun subscribeToLog(view: TextView) {
        logView = view
    }

    fun subscribeToState(view: TextView) {
        stateView = view
    }
}