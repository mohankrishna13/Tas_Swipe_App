package com.mohankrishna.swipeapp.UI.activities

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.mohankrishna.swipeapp.R


class AddProductsActivity : InternetConnectivityActivity() {
    var internetConnection=MutableLiveData<Boolean>()
    private val REQUEST_CODE_PERMISSIONS = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_products_screen)

        checkStoragePermission()

    }

    private fun checkStoragePermission() {
        ActivityCompat.requestPermissions(this, arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_MEDIA_IMAGES
            ), REQUEST_CODE_PERMISSIONS)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.size>0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED) || grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            } else if(grantResults[0]==PackageManager.PERMISSION_DENIED || grantResults[1]==PackageManager.PERMISSION_DENIED){
                var alertDialogBuilder=AlertDialog.Builder(this)
                alertDialogBuilder
                    .setMessage("Please Accept Storage Permissions"
                    )
                    .setCancelable(false)
                    .setPositiveButton(
                        "Allow",
                        DialogInterface.OnClickListener { dialog, id ->
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri: Uri =
                                Uri.fromParts("package", this.packageName, null)
                            intent.data = uri
                            startActivity(intent)
                        })
                    .setNegativeButton("Exit", DialogInterface.OnClickListener { dialog, id ->
                        finish()
                        dialog.cancel()
                    })
                val alertDialog: AlertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
        }
    }
    override fun getInternetConnection(b: Boolean) {
        internetConnection.postValue(b)
    }
}