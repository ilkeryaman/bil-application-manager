package com.yaman.ui.impl;

import com.yaman.bil.IBILManager;
import com.yaman.bil.helper.BILExecutionResult;
import com.yaman.ui.IUIUpdaterService;
import com.yaman.ui.constants.Messages;
import com.yaman.ui.enums.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UIUpdaterService implements IUIUpdaterService {

    @Autowired
    IBILManager bilManager;

    @Autowired
    UIGenerator uiGenerator;

    private boolean isLocked = false;

    @Override
    public void checkAndUpdateView() {
        if(!isLocked) {
            isLocked = true;
            BILExecutionResult checkResult = bilManager.check();
            isLocked = false;
            if (checkResult.isCommandSucceeded()) {
                boolean isBILRunning = (boolean) checkResult.getData();
                if (isBILRunning) {
                    uiGenerator.viewModeStarted();
                } else {
                    uiGenerator.viewModeStopped();
                }
            } else {
                uiGenerator.showMessage(Messages.CheckError, MessageType.ERROR);
            }
        }
    }

    @Override
    public void startAndUpdateView() {
        isLocked = true;
        uiGenerator.viewModeOperating();
        new Thread(() -> {
            BILExecutionResult startResult = bilManager.start();
            if(startResult.isCommandSucceeded()){
                BILExecutionResult checkStartedLogResult = bilManager.checkStartedLog();
                isLocked = false;
                uiGenerator.viewModeStarted();
                uiGenerator.showMessage(Messages.StartSucceeded + "\n" + checkStartedLogResult.getData().toString(),
                        checkStartedLogResult.isCommandSucceeded() ? MessageType.INFO : MessageType.WARNING);
            } else {
                isLocked = false;
                uiGenerator.viewModeStopped();
                uiGenerator.showMessage(Messages.StartError, MessageType.ERROR);
            }
        }).start();
    }

    @Override
    public void stopAndUpdateView() {
        isLocked = true;
        uiGenerator.viewModeOperating();
        new Thread(() -> {
            BILExecutionResult stopResult = bilManager.stop();
            isLocked = false;
            if(stopResult.isCommandSucceeded()){
                uiGenerator.viewModeStopped();
                uiGenerator.showMessage(Messages.StopSucceeded, MessageType.INFO);
            } else {
                uiGenerator.viewModeForcedStop();
                uiGenerator.showMessage(Messages.StopError, MessageType.ERROR);
            }
        }).start();
    }

    @Override
    public void forceStopAndUpdateView() {
        isLocked = true;
        uiGenerator.viewModeOperating();
        new Thread(() -> {
            BILExecutionResult stopResult = bilManager.forceStop();
            isLocked = false;
            if(stopResult.isCommandSucceeded()){
                uiGenerator.viewModeStopped();
                uiGenerator.showMessage(Messages.StopSucceeded, MessageType.INFO);
            } else {
                uiGenerator.viewModeForcedStop();
                uiGenerator.showMessage(Messages.StopError, MessageType.ERROR);
            }
        }).start();
    }
}
