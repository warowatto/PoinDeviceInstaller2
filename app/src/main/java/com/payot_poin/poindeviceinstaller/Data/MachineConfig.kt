package com.payot_poin.poindeviceinstaller.Data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

/**
 * Created by yongheekim on 2018. 3. 25..
 */

@Entity(tableName = "configs")
data class MachineConfig(
        @PrimaryKey(autoGenerate = true)
        val id: Int?,
        val name: String,
        val perPrice: Int,
        val perPulse: Int,
        val perPulseCount: Int,
        val minPrice: Int)