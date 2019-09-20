package callback;

/**
 * @Params:一个串口数据回调接口
 */
public interface SerialByteCallBack {
    void onSerialBytePortData(final byte[] buffer, final int size);
}