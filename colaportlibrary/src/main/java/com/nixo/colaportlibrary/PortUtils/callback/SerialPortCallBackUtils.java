package com.nixo.colaportlibrary.PortUtils.callback;

import android.util.Log;

public class SerialPortCallBackUtils {

    private static SerialCallBack mCallBack;

    public static void setCallBack(SerialCallBack callBack) {
        if(callBack == null){
            Log.e("DEBUG","callBack == null");
            return ;
        }
        mCallBack = callBack;
    }

    public static void doCallBackMethod(String info){
        if(mCallBack == null){
            Log.e("DEBUG","callBack == null");
            return ;
        }
        mCallBack.onSerialPortData(info);
    }
}