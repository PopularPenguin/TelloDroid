package com.popularpenguin.tellodroid.tello

import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Exception
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * UDP Client for sending and receiving commands
 */
class TelloCommand { // TODO: Rename TelloClient?

    private val ip = InetAddress.getByName("192.168.10.1")
    private val port = 8889
    private val socket = DatagramSocket(port)

    @Throws(Exception::class)
    suspend fun connect() {
        if (isConnected()) {
            return
        }

        socket.connect(ip, port)
        sendCommand("command")
    }

    // TODO: Move these over to the Tello class

    // Flip in a direction
    suspend fun flip(direction: Direction): String = sendCommand("flip ${direction.str}")

    // Fly to a point 20 - 500 cm away at 10 - 100 cm/s
    suspend fun go(x: Int, y: Int, z: Int, speed: Int): String {
        if (x or y or z !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }
        if (speed !in 10..100) {
            throw IllegalArgumentException("Invalid speed: Must be in range 10 - 100")
        }

        return sendCommand("go $x $y $z $speed")
    }

    // Fly a curve defined by the current and two given coordinates with speed (cm/s)
    // If the arc radius is not within the range of 0.5 - 10 meters, it responses false
    suspend fun curve(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int, speed: Int): String {
        if (x1 or y1 or z1 or x2 or y2 or z2 !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }
        if (speed !in 10..60) {
            throw IllegalArgumentException("Invalid speed: Must be in range 10 - 60")
        }

        return sendCommand("curve $x1 $y1 $z1 $x2 $y2 $z2 $speed")
    }

    // Set speed to 10 - 100 cm/s
    suspend fun speed(speed: Int): String {
        if (speed !in 10..100) {
            throw IllegalArgumentException("Invalid speed: Must be in range 10 - 100")
        }

        return sendCommand("speed $speed")
    }

    // Set WiFi with SSID and password
    suspend fun setWifi(ssid: String, password: String): String {
        return sendCommand("wifi $ssid $password")
    }

    // TODO: Implement read commands

    @Throws(IOException::class)
    suspend fun sendCommand(command: String): String {
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

    fun isConnected(): Boolean = socket.isConnected

    fun close() {
        socket.close()
    }

    enum class Direction(val str: String) {
        LEFT("l"),
        RIGHT("r"),
        FORWARD("f"),
        BACK("b")
    }
}