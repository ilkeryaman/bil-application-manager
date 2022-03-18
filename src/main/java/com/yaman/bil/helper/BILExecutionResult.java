package com.yaman.bil.helper;

public class BILExecutionResult {
    private boolean isCommandSucceeded;
    private Object data;

    public BILExecutionResult(boolean isCommandSucceeded){
        this.isCommandSucceeded = isCommandSucceeded;
    }

    public BILExecutionResult(boolean isCommandSucceeded, Object data){
        this.isCommandSucceeded = isCommandSucceeded;
        this.data = data;
    }

    public boolean isCommandSucceeded() {
        return isCommandSucceeded;
    }

    public Object getData() {
        return data;
    }
}
