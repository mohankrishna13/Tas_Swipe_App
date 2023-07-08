package com.mohankrishna.swipeapp.UI.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData

import com.mohankrishna.swipeapp.R

class ProductScreenActivity : InternetConnectivityActivity() {
    var internetIsConnected=MutableLiveData<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun getInternetConnection(b: Boolean) {
        internetIsConnected.postValue(b)
    }


}