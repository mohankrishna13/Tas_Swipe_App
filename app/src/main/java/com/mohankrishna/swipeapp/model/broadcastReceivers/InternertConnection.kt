package com.mohankrishna.swipeapp.model.broadcastReceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import com.mohankrishna.swipeapp.model.Interfaces.NetworkListener

class InternertConnection : BroadcastReceiver() {
    lateinit var networkListener: NetworkListener
    fun setListener(network: NetworkListener){
        this.networkListener=network
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if (ConnectivityManager.CONNECTIVITY_ACTION == intent!!.action) {
            val connectivityManager =
                context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            val check = networkInfo != null && networkInfo.isConnected
            networkListener.isConnected(check)
        }
    }

}