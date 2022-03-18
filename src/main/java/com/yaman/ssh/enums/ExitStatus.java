package com.yaman.ssh.enums;

public enum ExitStatus {
    SUCCESS(0);

    private int value;

    ExitStatus(int value){
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
