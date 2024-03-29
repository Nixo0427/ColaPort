package com.nixo.colaportlibrary.PortUtils.android_serialport_api;

import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {

    private static final String TAG = "SerialPort";
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {

        //检查访问权限，如果没有读写权限，进行文件操作，修改文件访问权限
        if (!device.canRead() || !device.canWrite()) {
            Log.e("Nixo---权限","拥有读写权限");
            try {
                //通过挂载到linux的方式，修改文件的操作权限
                Process su = Runtime.getRuntime().exec("/system/bin/su");
                String cmd = "chmod 666 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead() || !device.canWrite()) {
                    Log.e("Nixo----Port权限","Port:"+device+"没有权限");
                    throw new SecurityException();
                }else{
                    Log.e("Nixo----Port权限","Port:"+device+"拥有权限");
                }
            } catch (SecurityException e){
                Log.e("Nixo",device+"连接失败，未知错误");
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }else{
            Log.e("Nixo---权限","读写权限 ----------- Read:"+device.canRead()+"写"+device.canWrite());
        }

        mFd = open(device.getAbsolutePath(), baudrate, flags);

        if (mFd == null) {
            Log.e(TAG, "native open returns null");
            throw new IOException();
        }

        mFileInputStream = new FileInputStream(mFd);
        mFileOutputStream = new FileOutputStream(mFd);
    }

    // Getters and setters
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI(调用java本地接口，实现串口的打开和关闭)
    /**
     * 串口有五个重要的参数：串口设备名，波特率，检验位，数据位，停止位
     * 其中检验位一般默认位NONE,数据位一般默认为8，停止位默认为1
     */
    /**
     * @param path     串口设备的绝对路径
     * @param baudrate 波特率
     * @param flags    校验位
     */
    private native static FileDescriptor open(String path, int baudrate, int flags);
    public native void close();

    static {//加载jni下的C文件库
        System.loadLibrary("serial_port");
    }
}