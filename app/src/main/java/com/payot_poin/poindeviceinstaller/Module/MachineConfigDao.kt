package com.payot_poin.poindeviceinstaller.Module

import android.arch.persistence.room.*
import com.payot_poin.poindeviceinstaller.Data.MachineConfig

/**
 * Created by yongheekim on 2018. 3. 25..
 */
@Dao
interface MachineConfigDao {

    @Query("SELECT * FROM configs")
    fun getConfigs(): List<MachineConfig>

    @Query("SELECT * FROM configs WHERE id = :id")
    fun getConfigById(id: Int): MachineConfig

    @Insert
    fun add(machineConfig: MachineConfig)

    @Update
    fun update(machineConfig: MachineConfig)

    @Delete
    fun delete(machineConfig: MachineConfig)
}