package com.payot_poin.poindeviceinstaller.Interface

import android.bluetooth.BluetoothDevice
import io.reactivex.Observable

/**
 * Created by yongheekim on 2018. 3. 22..
 */
interface BleScanner {

    fun scanDevice(serviceUUID: String, timeout: Long): Observable<BluetoothDevice>

    fun scanDevice(servicesUUID: Array<String>, timeout: Long): Observable<BluetoothDevice>

    fun stopScanDevice()
}