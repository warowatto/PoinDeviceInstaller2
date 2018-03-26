package com.payot_poin.poindeviceinstaller.Module

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.payot_poin.poindeviceinstaller.Data.MachineConfig

/**
 * Created by yongheekim on 2018. 3. 25..
 */
@Database(entities = arrayOf(MachineConfig::class), version = 1, exportSchema = false)
abstract class MobileDatabase: RoomDatabase() {

    abstract fun configDao(): MachineConfigDao
}