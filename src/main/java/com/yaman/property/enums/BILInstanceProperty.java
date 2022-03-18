package com.yaman.property.enums;

public enum BILInstanceProperty {
    NAME("bil.instance.name");

    private String key;

    BILInstanceProperty(String key){
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }
}

