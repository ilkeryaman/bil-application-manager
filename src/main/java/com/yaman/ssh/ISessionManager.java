package com.yaman.ssh;

import com.yaman.ssh.helper.CommandResult;

public interface ISessionManager {
    void connect(String host, int port, String username, String password);
    void disconnect();
    boolean isConnected();
    CommandResult runCommand(String firstCommand, String ...otherCommands) throws Exception;
}
