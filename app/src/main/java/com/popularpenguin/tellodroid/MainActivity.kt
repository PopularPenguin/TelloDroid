package com.popularpenguin.tellodroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import com.popularpenguin.tellodroid.tello.Tello
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var tello: Tello

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tello = Tello()

        tello.subscribeToLog(logView)

        // TODO: Buttons are for testing only, maybe leave a land or emergency button on phone
        connectButton.setOnClickListener { tello.connect() }

        takeOffButton.setOnClickListener { tello.takeOff() }

        landButton.setOnClickListener { tello.land() }

        ccwButton.setOnClickListener { tello.rotate(-45) }

        cwButton.setOnClickListener { tello.rotate(45) }

        upButton.setOnClickListener { tello.up(20) }

        downButton.setOnClickListener { tello.down(20) }

        forwardButton.setOnClickListener { tello.forward(20) }

        backButton.setOnClickListener { tello.back(20) }

        leftButton.setOnClickListener { tello.left(20) }

        rightButton.setOnClickListener { tello.right(20) }

        flipButton.setOnClickListener { tello.flip("r") }

        emergencyButton.setOnClickListener { tello.emergency() }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        tello.onKeyDown(keyCode, event)

        return true
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        tello.onKeyUp(keyCode, event)

        return true
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        tello.onMotionEvent(event)

        return true
    }

    override fun onDestroy() {
        tello.close()

        super.onDestroy()
    }
}
