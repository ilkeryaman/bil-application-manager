package com.yaman.bil.enums;

public enum BILCommandResponse {
    CHECK_COMMAND_RESPONSE("-Djboss.home.dir=/opt/{bil.instance.name}/wildfly-8.1.0.Final"),
    STOP_COMMAND_RESPONSE("{\"outcome\" => \"success\"}");

    private String value;

    BILCommandResponse(String value){
        this.value = value;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }
}
