package android_serialport_api;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import callback.SerialByteCallBack;
import callback.SerialCallBack;
import callback.SerialPortCallBackUtils;
import util.ByteUtil;

/**
 * Created by Nixo
 * 2019-8-12 13:04:58.
 */

public class SerialPortUtil {

    public static String TAG = "SerialPortUtil";

    /**
     * 标记当前串口状态(true:打开,false:关闭)
     **/
    public static boolean isFlagSerial = false;
    public static SerialByteCallBack callBack;
    public static StringBuffer strBuffer  = new StringBuffer();
    public static SerialPort serialPort = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;
    public static Thread receiveThread = null;
    public static String strData = "";

    public  SerialPortUtil setCallBack(SerialByteCallBack callBack){
        SerialPortUtil.callBack = callBack;
        return this;
    }

    /**
     * 打开串口
     */
    public static boolean open(String pathname, int baudrate, int flags) {
        boolean isopen = false;
        if (isFlagSerial) {
            Log.e("Nixo---连接串口",isFlagSerial+"串口已经关闭不可再打开");
            return false;
        }
        try {
            serialPort = new SerialPort(new File(pathname), baudrate, flags);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            Log.e("Nixo---串口信息",pathname+":"+baudrate+"  flag:"+flags);
            receive();
            isopen = true;
            isFlagSerial = true;
        } catch (IOException e) {
            Log.e("Nixo---连接串口",e.toString());
            e.printStackTrace();
            isopen = false;
        }
        return isopen;
    }

    /**
     * 关闭串口
     */
    public static boolean close() {
        if (isFlagSerial) {
            Log.e("Nixo---连接串口",isFlagSerial+"串口已经关闭不可再打开");
            return false;
        }
        boolean isClose = false;
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            isClose = true;
            //关闭串口时，连接状态标记为false
            isFlagSerial = false;
        } catch (IOException e) {
            Log.e("Nixo---关闭串口",e.toString());
            e.printStackTrace();
            isClose = false;
        }
        return isClose;
    }

    /**
     * 发送串口指令
     */
    public static void sendString(String data) {
//        if (!isFlagSerial) {
//            return;
//        }
        try {
            Log.e("发送指令",ByteUtil.hex2byte(data).toString());
            outputStream.write(ByteUtil.hex2byte(data));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收串口数据的方法
     */
    public static void receive() {
        if (receiveThread != null && !isFlagSerial) {
            return;
        }
        receiveThread = new Thread() {
            @Override
            public void run() {


                while (isFlagSerial) {
                    try {
                        byte[] readData = new byte[64];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        if (size > 0 && isFlagSerial) {
                            onDataReceived(readData, size);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                  if(strBuffer.toString().toCharArray()[strBuffer.toString().toCharArray().length-1] == 'A'
                          && strBuffer.toString().toCharArray()[strBuffer.toString().toCharArray().length-2] == '0' ){
                      SerialPortCallBackUtils.doCallBackMethod(strBuffer.toString());
                    strBuffer.delete(0,strBuffer.length());

                    }

                }
            }
        };
        receiveThread.start();
    }
    protected static void onDataReceived(final byte[] buffer, final int size) {
        String result = "";
        if (strBuffer != null) {
            result  = ByteUtil.byteToStr(buffer, size);
//                    result = result + result;
//                    Log.e(TAG,"result="+result);
            strBuffer.append(result);//new String(buffer, 0, size));
        }
    }
}