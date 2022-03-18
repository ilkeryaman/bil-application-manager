package com.yaman.ssh.impl;

import com.yaman.ssh.ISessionManager;
import com.yaman.ssh.helper.CommandResult;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class SessionManager implements ISessionManager {

    private String delimeter = ";";
    Session _session = null;

    private Session getSession() {
        return _session;
    }

    private void setSession(Session session) {
        this._session = session;
    }

    @Override
    public void connect(String host, int port, String username, String password) {
        try {
            Session session = getSession();
            if (!isConnected()) {
                session = new JSch().getSession(username, host, port);
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
                session.connect();
            }
            setSession(session);
        }   catch (Exception exc) {

        }
    }

    @Override
    public void disconnect() {
        Session session = getSession();
        if(isConnected()){
            session.disconnect();
        }
    }

    @Override
    public boolean isConnected() {
        return _session != null && _session.isConnected();
    }

    @Override
    public CommandResult runCommand(String firstCommand, String... otherCommands) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ChannelExec channel = (ChannelExec) getSession().openChannel("exec");
        String commands = firstCommand.concat(delimeter).concat(String.join(delimeter, otherCommands));
        channel.setCommand(commands);
        channel.setErrStream(System.err);
        channel.setOutputStream(outputStream);
        channel.connect();
        while (channel.isConnected()) {
            Thread.sleep(100);
        }
        channel.disconnect();
        return new CommandResult(channel.getExitStatus(), outputStream);
    }
}
