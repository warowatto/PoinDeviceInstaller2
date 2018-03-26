package com.payot_poin.poindeviceinstaller.Module

import android.bluetooth.BluetoothDevice
import android.os.ParcelUuid
import com.payot_poin.poindeviceinstaller.Interface.BleScanner
import io.reactivex.Observable
import java.util.*

/**
 * Created by yongheekim on 2018. 3. 22..
 */
class PoinDeviceScanner : BleScanner {

    override fun scanDevice(serviceUUID: String, timeout: Long): Observable<BluetoothDevice> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun scanDevice(servicesUUID: Array<String>, timeout: Long): Observable<BluetoothDevice> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopScanDevice() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}