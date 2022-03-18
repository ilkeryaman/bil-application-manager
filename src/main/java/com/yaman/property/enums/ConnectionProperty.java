package com.yaman.property.enums;

public enum ConnectionProperty {
    HOST("ssh.host"),
    PORT("ssh.port"),
    USERNAME("ssh.username"),
    PASSWORD("ssh.password");

    private String key;

    ConnectionProperty(String key){
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }
}
