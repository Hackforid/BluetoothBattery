package com.smilehacker.bluetoothbattery

import android.bluetooth.*
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ble()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private fun ble() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val pairedDevices = bluetoothAdapter.bondedDevices
        Log.i(TAG, "is connect to headset = ${bluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED}")
        pairedDevices.forEach {
            Log.i(TAG, "name=${it.name} address=${it.address} bound=${it.bondState}  uuid=${it.uuids}")
            it.connectGatt(MainActivity@this, false, @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
            object: BluetoothGattCallback() {
                override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                    super.onConnectionStateChange(gatt, status, newState)
                    Log.i(TAG, "${it.name} gatt connect state change status=${status} new=${newState}")
                    val batteryService = gatt?.getService(BluetoothGattService.Battery_Service_UUID)
                }
            })
        }
    }
}
