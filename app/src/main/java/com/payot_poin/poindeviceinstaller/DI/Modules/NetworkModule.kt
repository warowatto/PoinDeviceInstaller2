package com.payot_poin.poindeviceinstaller.DI.Modules

import com.google.gson.GsonBuilder
import com.payot_poin.poindeviceinstaller.Interface.InstallAPI
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Created by yongheekim on 2018. 3. 26..
 */

@Module
class NetworkModule {

    private val HOST = "http://admin.payot-poin.com"

    @Singleton
    @Provides
    fun okhttp(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor())
                .build()
    }

    @Singleton
    @Provides
    fun retrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(HOST)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat(DateFormat.FULL).create()))
                .build()
    }

    @Singleton
    @Provides
    fun installService(retrofit: Retrofit): InstallAPI {
        return retrofit.create(InstallAPI::class.java)
    }
}