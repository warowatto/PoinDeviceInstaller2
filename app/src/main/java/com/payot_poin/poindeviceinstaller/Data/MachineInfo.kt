package com.payot_poin.poindeviceinstaller.Data

/**
 * Created by yongheekim on 2018. 3. 26..
 */
data class MachineInfo(
        val companyId: Int,
        val macAddress: String,
        val deviceName: String,
        val displayName: String,
        val typeId: Int,
        val description: String,
        val defaultPrice: Int,
        val isRunning: Boolean)