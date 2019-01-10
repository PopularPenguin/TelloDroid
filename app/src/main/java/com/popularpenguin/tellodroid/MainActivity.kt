package com.popularpenguin.tellodroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import com.popularpenguin.tellodroid.tello.Tello
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : AppCompatActivity() {

    //private val tello = Tello()

    // TODO: Delete
    lateinit private var ip: InetAddress
    private val port = 8889
    private lateinit var socket: DatagramSocket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //tello.connect()

        // TODO: Buttons are for testing only, maybe leave a land or emergency button on phone
        connectButton.setOnClickListener { connect() }

        takeOffButton.setOnClickListener {
            GlobalScope.launch {
                command("takeoff")
            }
        }

        landButton.setOnClickListener {
            GlobalScope.launch {
                command("land")
            }
        }

        emergencyButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                command("emergency")
            }
        }
    }

    // TODO: Delete
    private fun connect() {
        GlobalScope.launch {
            ip = InetAddress.getByName("192.168.10.1")
            socket = DatagramSocket(port)
            socket.connect(ip, port)
            command("command")
        }
    }

    // TODO: Delete
    private suspend fun command(command: String): String {
        if (command.isEmpty()) {
            return "Empty command"
        }
        if (!socket.isConnected) {
            return "Disconnected"
        }

        var response = "ERROR"

        val job = GlobalScope.launch {
            val receiveData = ByteArray(1024)
            val sendData = command.toByteArray()

            val sendPacket = DatagramPacket(sendData, sendData.size, ip, port)
            socket.send(sendPacket)

            val receivePacket = DatagramPacket(receiveData, receiveData.size)
            socket.receive(receivePacket)

            response = receivePacket.data.toString()
        }

        job.join()

        return response
    }

    /*

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
    } */
}
