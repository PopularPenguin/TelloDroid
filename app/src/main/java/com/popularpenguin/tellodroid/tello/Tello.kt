package com.popularpenguin.tellodroid.tello

import android.view.InputDevice
import android.view.KeyEvent
import android.view.MotionEvent
import com.popularpenguin.tellodroid.controller.Controller
import com.popularpenguin.tellodroid.controller.XboxController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Class acting as a view model
 */
class Tello {

    // TODO: Move individual commands to this class
    private val command = TelloCommand()
    private val state = TelloState()
    // TODO: Add video stream here

    private val controller = XboxController()

    private var telloCommand = "NULL"

    fun connect() {
        GlobalScope.launch {
            command.connect()
            //state.connect()
        }
    }

    fun sendCommand(cmd: String) {
        GlobalScope.launch {
            command.sendCommand(cmd)
        }
    }

    fun onKeyDown(keyCode: Int, event: KeyEvent) {
        if (event.source and InputDevice.SOURCE_GAMEPAD == InputDevice.SOURCE_GAMEPAD) {
            telloCommand = controller.processDigital(keyCode)
            GlobalScope.launch {
                command.sendCommand(telloCommand)
            }
        }
    }

    fun onKeyUp(keyCode: Int, event: KeyEvent) {
        // TODO: Probably don't have to do anything in this function
    }

    fun onMotionEvent(event: MotionEvent) {

    }

    fun close() {
        command.close()
        state.close()
    }

}