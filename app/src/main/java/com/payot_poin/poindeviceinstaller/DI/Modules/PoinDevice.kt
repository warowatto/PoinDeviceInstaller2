package com.payot_poin.poindeviceinstaller.DI.Modules

import android.bluetooth.*
import android.content.Context
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.toObservable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.TimeUnit
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by yongheekim on 2018. 3. 22..
 */
class PoinDevice() : BluetoothGattCallback() {

    /**
     *
     */
    enum class PoinDeviceConnectState {
        CONNECT, DISCONNECT, READY, AUTH_OK, AUTH_FAIL
    }

    /**
     * 접속할 UUIDS
     */
    private val serviceUUID = "0000DE01-0000-1000-8000-00805F9B34FB"
    private val charUUID = "0000DEC1-0000-1000-8000-00805F9B34FB"

    /**
     * 장치의 상태 리스너
     */
    private val onStateChangeObserver = PublishSubject.create<PoinDeviceConnectState>()
    private val onResponseMessage = PublishSubject.create<Byte>()

    /**
     *
     */
    private var service: Pair<BluetoothGatt, BluetoothGattCharacteristic>? = null

    /**
     * 암호화 부분
     */
    private val key = byteArrayOf(
            0x2B, 0x7E, 0x15, 0x16, 0x28,
            0xAE.toByte(), 0xD2.toByte(), 0xA6.toByte(), 0xAB.toByte(), 0xF7.toByte(),
            0x15, 0x88.toByte(), 0x09, 0xCF.toByte(), 0x4F,
            0x3C)

    private val iv = byteArrayOf(
            0x00, 0x01, 0x02, 0x03, 0x04,
            0x05, 0x06, 0x07, 0x08, 0x09,
            0x0A, 0x0B, 0x0C, 0x0D, 0x0E,
            0x0F)

    private val cipher: Cipher = Cipher.getInstance("AES/CBC/NoPadding")
    private val keySpec = SecretKeySpec(key, "AES")
    private val ivSpec = IvParameterSpec(iv)

    fun connect(context: Context, bluetoothDevice: BluetoothDevice): Observable<PoinDeviceConnectState> {
        bluetoothDevice.connectGatt(context, false, this)

        return onStateChangeObserver
    }

    fun sendCommend(cmd: String): Single<List<String>> {
        return sendMessage(sendMessageFormat(cmd))
                .map { parssingMessage(it) }
    }

    fun disconnect() {
        service?.first?.close()
    }

    private fun sendMessage(byteArray: ByteArray): Single<ByteArray> {
        val message = byteArray.toObservable().buffer(20).map { it.toByteArray() }
        val messageOffset = Observable.interval(0, 50, TimeUnit.MILLISECONDS, Schedulers.computation())

        return message.zipWith(messageOffset) { msg, _ ->
            service?.second?.value = msg
            service?.first?.writeCharacteristic(service?.second)
            return@zipWith msg
        }.takeLast(1)
                .flatMap { onResponseMessage.buffer(64).take(1).map { it.toByteArray() } }
                .timeout(5, TimeUnit.SECONDS, Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .singleOrError()
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)

        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {
                onStateChangeObserver.onNext(PoinDeviceConnectState.CONNECT)
                gatt?.discoverServices()
            }
            BluetoothProfile.STATE_DISCONNECTED -> onStateChangeObserver.onNext(PoinDeviceConnectState.DISCONNECT)
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)
        this.service = gatt!! to gatt.getService(UUID.fromString(serviceUUID)).getCharacteristic(UUID.fromString(charUUID))

        if (this.service?.first?.setCharacteristicNotification(this.service?.second, true)
                        ?: false) {
            onStateChangeObserver.onNext(PoinDeviceConnectState.READY)
        }
    }

    private fun encryption(byteArray: ByteArray): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        return cipher.doFinal(byteArray)
    }

    private fun decrpytion(byteArray: ByteArray): ByteArray {
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

        return cipher.doFinal(byteArray)
    }

    private fun sendMessageFormat(cmd: String): ByteArray {
        val content = cmd.toByteArray(Charsets.UTF_8)
        val totalMessageSize = 64
        val firstRandomSize = 4
        val contentSize = content.size
        val checksum = crc(content)
        val lastRandomSize = totalMessageSize - firstRandomSize - contentSize - checksum.size

        val randomByte = SecureRandom()
        return encryption(byteArrayOf(
                *randomByte.generateSeed(firstRandomSize),
                *content,
                *checksum,
                *randomByte.generateSeed(lastRandomSize)))
    }

    private fun parssingMessage(byteArray: ByteArray): List<String> {

        val message = decrpytion(byteArray).drop(4)
        val validationCrcValue = byteArrayOf(0x00, 0x00)

        var resultBytes: ByteArray = byteArrayOf()

        checksum@
        for ((index, _) in message.withIndex()) {
            val nowMessage = message.take(index + 1).toByteArray()
            if (crc(nowMessage) contentEquals validationCrcValue) {
                resultBytes = nowMessage.dropLast(2).toByteArray()
                break@checksum
            }
        }

        return resultBytes.toString(Charsets.UTF_8).split(" ")
    }

    private fun crc(byteArray: ByteArray): ByteArray {
        var crc = 0xffff
        var flag: Int

        for (b in byteArray) {
            crc = crc xor (b.toInt() and 0xff)

            for (i in 0..7) {
                flag = crc and 1
                crc = crc shr 1
                if (flag != 0) {
                    crc = crc xor 0xa001
                }
            }
        }

        // println(crc)

        val buffer = ByteBuffer.allocate(2)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        while (buffer.hasRemaining()) {
            buffer.putShort(crc.toShort())
        }

        return buffer.array()
    }

}