package com.payot_poin.poindeviceinstaller.Activites

import android.bluetooth.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * Created by yongheekim on 2018. 3. 22..
 */
class TestActivity : AppCompatActivity() {

    val callback = object : BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> gatt?.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            for (service in gatt?.services!!) {
                for (char in service.characteristics) {
                    if (isWriteable(char) && isNotify(char)) {
                        Log.d("SERVICE UUID", service.uuid.toString())
                        Log.d("CHAR UUID", char.uuid.toString())
                    }
                }
            }
        }

        fun isWriteable(characteristic: BluetoothGattCharacteristic): Boolean {
            return (characteristic.properties and (BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0
        }

        fun isNotify(characteristic: BluetoothGattCharacteristic): Boolean {
            return (characteristic.properties and (BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice("D4:36:39:BC:17:A6")

        device.connectGatt(this, false, callback)
    }
}