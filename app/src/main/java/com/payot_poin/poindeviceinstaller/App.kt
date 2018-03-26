package com.payot_poin.poindeviceinstaller

import android.app.Application
import com.payot_poin.poindeviceinstaller.DI.Components.ApplicationComponent
import com.payot_poin.poindeviceinstaller.DI.Components.DaggerApplicationComponent
import com.payot_poin.poindeviceinstaller.DI.Modules.NetworkModule

/**
 * Created by yongheekim on 2018. 3. 26..
 */
class App : Application() {

    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerApplicationComponent.builder()
                .networkModule(NetworkModule())
                .build()
    }
}