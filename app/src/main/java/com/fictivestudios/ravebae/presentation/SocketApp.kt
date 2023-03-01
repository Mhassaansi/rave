package com.gigo.clean.networksetup.socket

import io.socket.client.IO
import com.gigo.clean.networksetup.socket.SocketApp
import io.socket.client.Socket
import java.lang.Exception
import java.lang.RuntimeException

class SocketApp {
    //http://88.208.220.41:9000
    //name=1234&type=Android,DriverApp
    val socket: Socket
        get() {
            var mSocket: Socket
            run {
                try {
                    val options = IO.Options()
                    options.query = ""
                    mSocket = IO.socket("https://webservices.ravebae.org:3010/")
                    // https://server.appsstaging.com:3010/
                    //http://88.208.220.41:9000
                    //name=1234&type=Android,DriverApp
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
            return mSocket
        }

    companion object {
        var socketApp: SocketApp? = null
        val instance: SocketApp?
            get() {
                if (socketApp == null) {
                    socketApp = SocketApp()
                }
                return socketApp
            }
    }
}