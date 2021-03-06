package com.popularpenguin.tellodroid.tello

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException

/**
 * UDP Client for sending and receiving commands
 */
class TelloClient {

    private val ip = InetAddress.getByName("192.168.10.1")
    private val port = 8889
    private var socket = DatagramSocket(port)

    @Volatile
    private var queuedCommands = 0 // Number of commands sent to Tello

    @Throws(Exception::class)
    suspend fun connect() {
        if (!socket.isConnected) {
            socket.connect(ip, port)
        }

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

    /** @return The Tello's response to the sent command (ok, error, or a state variable */
    @Throws(IOException::class)
    suspend fun sendCommand(command: String): String {
        queuedCommands++

        if (command.isEmpty()) {
            return "Empty command"
        }
        if (!socket.isConnected) {
            return "Disconnected"
        }

        while (queuedCommands > 3) {
            delay(50)

            if (queuedCommands <= 3) break
        }

        return withContext(Dispatchers.IO) {
            val receiveData = ByteArray(1024)
            val sendData = command.toByteArray()

            val sendPacket = DatagramPacket(sendData, sendData.size, ip, port)
            socket.send(sendPacket)

            try {
                val receivePacket = DatagramPacket(receiveData, receiveData.size)
                socket.receive(receivePacket)

                queuedCommands--

                String(receivePacket.data)
            } catch (e: SocketException) {
                connect()

                "SocketException"
            }
        }
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