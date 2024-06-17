package com.me.recipe.domain.util

import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import javax.net.SocketFactory

/**
 * Send a ping to googles primary DNS.
 * If successful, that means we have internet.
 */
object DoesNetworkHaveInternet {

    // Make sure to execute this on a background thread.
    fun execute(socketFactory: SocketFactory): Boolean {
        return try {
            Log.d("DoesNetworkHaveInternet", "PINGING google.")
            val socket = socketFactory.createSocket() ?: throw IOException("Socket is null.")
            socket.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            socket.close()
            Log.d("DoesNetworkHaveInternet", "PING success.")
            true
        } catch (e: IOException) {
            Log.e("DoesNetworkHaveInternet", "No internet connection. $e")
            false
        }
    }
}
