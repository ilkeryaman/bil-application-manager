package com.yaman.bil.impl;

import com.yaman.bil.IBILManager;
import com.yaman.bil.ILogExtractor;
import com.yaman.bil.enums.BILCommand;
import com.yaman.bil.enums.BILCommandResponse;
import com.yaman.bil.helper.BILExecutionResult;
import com.yaman.bil.helper.ByteOperations;
import com.yaman.bil.constants.Messages;
import com.yaman.property.IPropertyManager;
import com.yaman.property.enums.ConnectionProperty;
import com.yaman.ssh.ISessionManager;
import com.yaman.ssh.enums.ExitStatus;
import com.yaman.ssh.helper.CommandResult;
import com.yaman.util.DateUtils;
import com.yaman.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

@Service
public class BILManager implements IBILManager {

    @Autowired
    ISessionManager sessionManager;

    @Autowired
    IPropertyManager propertyManager;

    @Autowired
    ILogExtractor logExtractor;

    @Value("${bil.startlog.check.count}")
    private int startLogCheckCount;

    @Value("${bil.startlog.check.delay}")
    private int startLogCheckDelay;

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
    public BILExecutionResult checkStartedLog() {
        boolean isSucceeded = false;
        String message = null;
        try {
            BILExecutionResult checkResult = check();
            if(checkResult.isCommandSucceeded()) {
                if((boolean) checkResult.getData()){
                    BILExecutionResult getLastLogTimeResult = getLastLogTime();
                    if(getLastLogTimeResult.isCommandSucceeded()){
                        Timestamp lastLogTime = DateUtils.convertToTimestamp(getLastLogTimeResult.getData().toString());
                        if(lastLogTime != null){
                            BILExecutionResult getStartedLogResult = getStartedLog(0, lastLogTime);
                            if(getStartedLogResult.isCommandSucceeded()){
                                isSucceeded = true;
                                message = getStartedLogResult.getData().toString();
                            } else {
                                message = Messages.CheckLogError;
                            }
                        }
                    } else {
                        message = Messages.CheckLogError;
                    }
                } else {
                    message = Messages.NotRunning;
                }
            } else {
                message = Messages.CheckLogError;
            }
        } catch (Exception exc) {
            message = Messages.CheckLogError;
        } finally {
            return new BILExecutionResult(isSucceeded, message);
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

    private BILExecutionResult getLastLogTime() {
        boolean isSucceeded = false;
        String dateStr = null;
        try {
            CommandResult commandResult = sessionManager.runCommand(BILCommand.GET_LAST_LOG_TIME.getCommand());
            String responseText = new String(commandResult.getByteArrayOutputStream().toByteArray());
            dateStr = responseText;
            isSucceeded = commandResult.getExitStatus() == ExitStatus.SUCCESS.getValue() && StringUtils.isNotEmpty(dateStr);
        } catch (Exception exc){

        } finally {
            return new BILExecutionResult(isSucceeded, dateStr);
        }
    }

    private BILExecutionResult getStartedLog(int currentCheckNumber, Timestamp lastLogTime) {
        boolean isSucceeded = false;
        String responseText = null;
        try {
            CommandResult commandResult = sessionManager.runCommand(BILCommand.GET_STARTED_LOG.getCommand());
            responseText = new String(commandResult.getByteArrayOutputStream().toByteArray());
            if(commandResult.getExitStatus() == ExitStatus.SUCCESS.getValue()){
                Timestamp startedLogDate = logExtractor.extractDate(responseText);
                if(startedLogDate != null && lastLogTime.compareTo(startedLogDate) < 0){
                    isSucceeded = true;
                } else {
                    if(currentCheckNumber < startLogCheckCount){
                        Thread.sleep(startLogCheckDelay);
                        BILExecutionResult startedLogResult = getStartedLog(currentCheckNumber+1, lastLogTime);
                        isSucceeded = startedLogResult.isCommandSucceeded();
                        if(StringUtils.isNotEmpty(startedLogResult.getData())) {
                            responseText = startedLogResult.getData().toString();
                        }
                    }
                }
            }
        } catch (Exception exc){

        } finally {
            return new BILExecutionResult(isSucceeded, responseText);
        }
    }
}
