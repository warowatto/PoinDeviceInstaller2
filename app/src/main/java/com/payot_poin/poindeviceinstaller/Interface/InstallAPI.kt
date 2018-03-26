package com.payot_poin.poindeviceinstaller.Interface

import com.payot_poin.poindeviceinstaller.Data.MachineConfig
import com.payot_poin.poindeviceinstaller.Data.MachineInfo
import com.payot_poin.poindeviceinstaller.Data.User
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by yongheekim on 2018. 3. 25..
 */
interface InstallAPI {

    @FormUrlEncoded
    @POST("/api/install/user")
    fun login(@Field("email") email: String,
              @Field("password") password: String): Single<User>

    @FormUrlEncoded
    @POST("/api/machine/install")
    fun installMachine(
            @Body config: MachineInfo
    ): Single<Pair<String, String>>
}