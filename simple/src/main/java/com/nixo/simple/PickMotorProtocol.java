package com.nixo.simple;

import com.nixo.colaportlibrary.PortUtils.BaseProtocol;

import org.jetbrains.annotations.NotNull;

public final class PickMotorProtocol extends BaseProtocol {

    public static final int CLOSE = 1;
    public static final int OPEN = 2;
    private final int dir;
    private final int id;

    public PickMotorProtocol(int paramInt1, int paramInt2) {
        this.id = paramInt1;
        this.dir = paramInt2;
    }

    public final int getDir() {
        return this.dir;
    }

    public final int getId() {
        return this.id;
    }

    @NotNull
    public byte[] toByteArray() {
        return new byte[]{27, 7, (byte) 162, (byte) this.id, (byte) this.dir, (byte)15, 10};
    }
}
