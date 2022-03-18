package com.yaman.ssh.helper;

import java.io.ByteArrayOutputStream;

public class CommandResult {
    private int exitStatus;
    private ByteArrayOutputStream byteArrayOutputStream;

    public CommandResult(int exitStatus, ByteArrayOutputStream byteArrayOutputStream){
        this.exitStatus = exitStatus;
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public int getExitStatus() {
        return exitStatus;
    }

    public ByteArrayOutputStream getByteArrayOutputStream() {
        return byteArrayOutputStream;
    }
}
