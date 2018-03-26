package com.payot_poin.poindeviceinstaller.DI.Components

import android.app.Application
import com.payot_poin.poindeviceinstaller.DI.Modules.ApplicationModule
import com.payot_poin.poindeviceinstaller.DI.Modules.NetworkModule
import com.payot_poin.poindeviceinstaller.Interface.InstallAPI
import dagger.Component
import javax.inject.Singleton

/**
 * Created by yongheekim on 2018. 3. 26..
 */

@Singleton
@Component(modules = arrayOf(ApplicationModule::class, NetworkModule::class))
interface ApplicationComponent {

    fun inject(app: Application)

    fun api(): InstallAPI
}