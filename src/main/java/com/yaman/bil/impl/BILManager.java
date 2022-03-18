package com.yaman.bil.impl;

import com.yaman.bil.IBILManager;
import com.yaman.bil.enums.BILCommand;
import com.yaman.bil.enums.BILCommandResponse;
import com.yaman.bil.helper.BILExecutionResult;
import com.yaman.bil.helper.ByteOperations;
import com.yaman.property.IPropertyManager;
import com.yaman.property.enums.ConnectionProperty;
import com.yaman.ssh.ISessionManager;
import com.yaman.ssh.enums.ExitStatus;
import com.yaman.ssh.helper.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class BILManager implements IBILManager {

    @Autowired
    ISessionManager sessionManager;

    @Autowired
    IPropertyManager propertyManager;

    @Override
    public void connect() {
        Properties props = propertyManager.getProperties();
        sessionManager.connect(
                props.getProperty(ConnectionProperty.HOST.getKey()),
                Integer.parseInt(props.getProperty(ConnectionProperty.PORT.getKey())),
                props.getProperty(ConnectionProperty.USERNAME.getKey()),
                props.getProperty(ConnectionProperty.PASSWORD.getKey()));
    }

    @Override
    public void disconnect() {
        sessionManager.disconnect();
    }

    @Override
    public BILExecutionResult start() {
        boolean isSucceeded = false, isAlreadyRunning = false;
        try{
            BILExecutionResult checkResult = check();
            if(checkResult.isCommandSucceeded()) {
                if((boolean) checkResult.getData()){
                    isSucceeded = isAlreadyRunning = true;
                } else {
                    CommandResult commandResult = sessionManager.runCommand(BILCommand.START.getCommand());
                    isSucceeded = commandResult.getExitStatus() == ExitStatus.SUCCESS.getValue();
                }
            }
        } catch (Exception exc) {

        } finally {
            return new BILExecutionResult(isSucceeded, isAlreadyRunning);
        }
    }

    @Override
    public BILExecutionResult stop() {
        boolean isSucceeded = false, isAlreadyRunning = true;
        try{
            BILExecutionResult checkResult = check();
            if(checkResult.isCommandSucceeded()) {
                if((boolean) checkResult.getData()){
                    CommandResult commandResult = sessionManager.runCommand(BILCommand.SHUT_DOWN.getCommand());
                    String responseText = new String(commandResult.getByteArrayOutputStream().toByteArray());
                    isSucceeded = commandResult.getExitStatus() == ExitStatus.SUCCESS.getValue()
                            && responseText.replaceAll("\\n","").equals(BILCommandResponse.STOP_COMMAND_RESPONSE.getValue());
                } else {
                    isSucceeded = true;
                    isAlreadyRunning = false;
                }
            }
        } catch (Exception exc) {

        } finally {
            return new BILExecutionResult(isSucceeded, isAlreadyRunning);
        }
    }

    @Override
    public BILExecutionResult forceStop() {
        boolean isSucceeded = false, isAlreadyRunning = true;
        try{
            BILExecutionResult checkResult = check();
            if(checkResult.isCommandSucceeded()) {
                if((boolean) checkResult.getData()){
                    CommandResult commandResult = sessionManager.runCommand(BILCommand.FORCE_STOP.getCommand());
                    String responseText = new String(commandResult.getByteArrayOutputStream().toByteArray());
                    isSucceeded = commandResult.getExitStatus() == ExitStatus.SUCCESS.getValue()
                            && responseText.replaceAll("\\n","").equals(BILCommandResponse.STOP_COMMAND_RESPONSE.getValue());
                } else {
                    isSucceeded = true;
                    isAlreadyRunning = false;
                }
            }
        } catch (Exception exc) {

        } finally {
            return new BILExecutionResult(isSucceeded, isAlreadyRunning);
        }
    }

    @Override
    public BILExecutionResult check() {
        boolean isSucceeded = false, isRunning = false;
        try{
            CommandResult commandResult = sessionManager.runCommand(BILCommand.CHECK.getCommand());
            List<String> lines = ByteOperations.getLines(commandResult.getByteArrayOutputStream().toByteArray());
            isRunning = lines.stream().anyMatch(line -> line.contains(BILCommandResponse.CHECK_COMMAND_RESPONSE.getValue()));
            isSucceeded = commandResult.getExitStatus() == ExitStatus.SUCCESS.getValue();
        } catch (Exception exc) {

        } finally {
            return new BILExecutionResult(isSucceeded, isRunning);
        }
    }
}
