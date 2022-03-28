package com.yaman.bil;

import com.yaman.bil.helper.BILExecutionResult;

public interface IBILManager {
    void connect();
    void disconnect();
    BILExecutionResult start();
    BILExecutionResult checkStartedLog();
    BILExecutionResult stop();
    BILExecutionResult forceStop();
    BILExecutionResult check();
}
