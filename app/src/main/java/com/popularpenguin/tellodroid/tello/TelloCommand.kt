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
class TelloCommand {

    // TODO: Get this to connect to drone, test takeOff() and land() first
    private val ip = InetAddress.getByName("192.168.10.1")
    private val port = 8889
    private val socket = DatagramSocket(port)

    @Throws(Exception::class)
    suspend fun connect() {
        socket.connect(ip, port)
        sendCommand("command")
    }

    suspend fun takeOff(): String = sendCommand("takeoff")

    suspend fun land(): String = sendCommand("land")

    // Stop all motors immediately
    suspend fun emergency(): String = sendCommand("emergency")

    // Fly up 20-500 cm
    suspend fun up(distance: Int): String {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        return sendCommand("up $distance")
    }

    // Fly down 20-500 cm
    suspend fun down(distance: Int): String {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        return sendCommand("down $distance")
    }

    // Fly left 20-500 cm
    suspend fun left(distance: Int): String {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        return sendCommand("left $distance")
    }

    // Fly right 20-500 cm
    suspend fun right(distance: Int): String {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        return sendCommand("right $distance")
    }

    // Fly forward 20-500 cm
    suspend fun forward(distance: Int): String {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        return sendCommand("forward $distance")
    }

    // Fly back 20-500 cm
    suspend fun back(distance: Int): String {
        if (distance !in 20..500) {
            throw IllegalArgumentException("Invalid distance: Must be in range 20 - 500")
        }

        return sendCommand("back $distance")
    }

    // Rotate clockwise 1-3600 degrees
    suspend fun rotateClockwise(degrees: Int): String {
        if (degrees !in 1..3600) {
            throw IllegalArgumentException("Invalid degrees: Must be in range 1 - 3600")
        }

        return sendCommand("cw $degrees")
    }

    // Rotate counter-clockwise 1-3600 degrees
    suspend fun rotateCounterClockwise(degrees: Int): String {
        if (degrees !in 1..3600) {
            throw IllegalArgumentException("Invalid degrees: Must be in range 1 - 3600")
        }

        return sendCommand("ccw $degrees")
    }

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

        val job = GlobalScope.launch(Dispatchers.Main) {
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