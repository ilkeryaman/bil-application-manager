package com.yaman.scheduler;

import com.yaman.bil.IBILManager;
import com.yaman.ui.IUIUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class StatusCheckerJob {

    @Autowired
    IBILManager bilManager;

    @Autowired
    IUIUpdaterService uiUpdaterService;

    @Value("${scheduler.check.enabled}")
    private boolean isCheckEnabled;

    @Scheduled(fixedRateString="${scheduler.check.duration}", initialDelayString = "${scheduler.check.duration}")
    public void checkStatus(){
        if(isCheckEnabled){
            uiUpdaterService.checkAndUpdateView();
        }
    }
}
