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

        // TODO: Buttons are for testing only, maybe leave a land or emergency button on phone
        connectButton.setOnClickListener { tello.connect() }

        takeOffButton.setOnClickListener {
            tello.sendCommand("takeoff")
        }

        landButton.setOnClickListener {
            tello.sendCommand("land")
        }

        emergencyButton.setOnClickListener {
            tello.sendCommand("emergency")
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        tello.onKeyDown(keyCode, event)

        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        tello.onKeyUp(keyCode, event)

        return super.onKeyUp(keyCode, event)
    }

    override fun onGenericMotionEvent(event: MotionEvent): Boolean {
        tello.onMotionEvent(event)

        return super.onGenericMotionEvent(event)
    }

    override fun onDestroy() {
        tello.close()

        super.onDestroy()
    }
}
