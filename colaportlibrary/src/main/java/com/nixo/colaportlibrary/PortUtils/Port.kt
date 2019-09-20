package com.nixo.tyndkprojcet.Utils.PortUtils

import android_serialport_api.SerialPortUtil
import callback.SerialCallBack
import callback.SerialPortCallBackUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


object  Port {

    fun Instance():Port= this
    private var pathName:String = "/dev/ttymxc2"
    private var baudrate:Int = 9600
    private var flag:Int = 0

    fun init(pathName:String , baudrate:Int,flag:Int):Port=this.apply {
        this.pathName = pathName
        this.baudrate = baudrate
        this.flag = flag
    }

    @Throws(IOException::class)
    fun read(paramArrayOfByte: ByteArray): Int {
        return SerialPortUtil.inputStream.read(paramArrayOfByte)
    }

    @Throws(IOException::class)
    fun write(paramArrayOfByte: ByteArray) :Port{
        GlobalScope.launch (Dispatchers.IO){
            SerialPortUtil.open(pathName, baudrate, flag)
            SerialPortUtil.outputStream.write(paramArrayOfByte)
            SerialPortUtil.inputStream.read(paramArrayOfByte)
        }
        return this
    }

    fun reply(reply : SerialCallBack){
        SerialPortCallBackUtils.setCallBack(reply)
    }


}