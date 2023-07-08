package com.mohankrishna.swipeapp.UI.activities

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mohankrishna.swipeapp.R
import com.mohankrishna.swipeapp.model.Interfaces.NetworkListener
import com.mohankrishna.swipeapp.model.broadcastReceivers.InternertConnection

abstract class InternetConnectivityActivity : AppCompatActivity(), NetworkListener {
    private lateinit var internertConnection: InternertConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internet_connectivity)
        internertConnection= InternertConnection()
        internertConnection.setListener(this)
        callBroadCastReceiver()
       
    }

    abstract fun getInternetConnection(boolean: Boolean)
    //Initializing Broadcast receiver for checking internet connection
    private fun callBroadCastReceiver() {
        var intentFilter= IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(internertConnection, intentFilter)
    }
    //Once App Destroys needs to unregister
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(internertConnection)
    }

    override fun isConnected(data: Boolean) {
       getInternetConnection(data)
    }

}