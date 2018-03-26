package com.payot_poin.poindeviceinstaller.Activites

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.payot_poin.poindeviceinstaller.App
import com.payot_poin.poindeviceinstaller.Interface.InstallAPI
import com.payot_poin.poindeviceinstaller.R

/**
 * Created by yongheekim on 2018. 3. 26..
 */
class MachineConfigListActivity : AppCompatActivity() {

    lateinit var api:InstallAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configlist)

        api = App.component.api()

    }
}