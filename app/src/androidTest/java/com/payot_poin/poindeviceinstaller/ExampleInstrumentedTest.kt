package com.payot_poin.poindeviceinstaller

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import com.payot_poin.poindeviceinstaller.Data.MachineConfig
import com.payot_poin.poindeviceinstaller.Module.MobileDatabase
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.math.log

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    var database: MobileDatabase? = null

    @Before
    fun init() {
        val context = InstrumentationRegistry.getContext()
        database = Room.databaseBuilder(context, MobileDatabase::class.java, "machine")
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun destroy() {
        database?.close()
    }

    @Test
    fun useAppContext() {
        println("테스트 시작")

        val item = MachineConfig(null, "이름", 500, 100, 2, 1000)
        database?.configDao()?.add(item)

        Log.d("data", database?.configDao()?.getConfigs().toString())

        assertNotNull(database?.configDao()?.getConfigs())

        println("테스트 종료")
    }
}
