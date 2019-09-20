package com.nixo.simple

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import callback.SerialCallBack
import com.nixo.tyndkprojcet.Utils.PortUtils.Port

class MainActivity : AppCompatActivity(),SerialCallBack {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        
        Port.Instance()
            .init("/dev/ttymxc2",9600,0)
            .write(PickMotorProtocol(0,1).toByteArray())
            .reply(SerialCallBack {
            Log.e("CallBack","串口返回值为->$it")
        })



        Port.Instance()
            .init("/dev/ttymxc2",9600,0)
            .write(PickMotorProtocol(0,1).toByteArray())
            .reply(this)
        
    }

    override fun onSerialPortData(serialPortData: String?) {
        Log.e("CallBack","串口返回值为->$serialPortData")

    }




}
