package com.payot_poin.poindeviceinstaller.Activites

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.payot_poin.poindeviceinstaller.App
import com.payot_poin.poindeviceinstaller.Interface.InstallAPI
import com.payot_poin.poindeviceinstaller.R

/**
 * Created by yongheekim on 2018. 3. 22..
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var api: InstallAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        api = App.component.api()

        checkPermissions()
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_FINE_LOCATION
        )
        ActivityCompat.requestPermissions(this, permissions, 2000)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val resultCount = grantResults.filter { it == PackageManager.PERMISSION_GRANTED }.count()

        if (requestCode == 2000 && resultCount == 4) {
            val intent = Intent(this, InstallActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, resultCount.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}