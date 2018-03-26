package com.payot_poin.poindeviceinstaller.DI.Modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by yongheekim on 2018. 3. 26..
 */

@Module
class ApplicationModule(val app: Application) {

    @Singleton
    @Provides
    fun context(): Context = app.applicationContext

    @Singleton
    @Provides
    fun preference(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(app.applicationContext)


}