package com.popularpenguin.tellodroid.tello

import java.lang.Exception
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * UDP Server to receive state
 */
class TelloState {

    private val ip = InetAddress.getByName("192.168.10.1")
    private val port = 8890
    private val socket = DatagramSocket(port)

    @Throws(Exception::class)
    suspend fun connect() {
        socket.connect(ip, port)
        sendCommand("command")
    }

    private fun sendCommand(command: String): String {
        return "TODO"
    }

    fun close() {
        socket.close()
    }
}