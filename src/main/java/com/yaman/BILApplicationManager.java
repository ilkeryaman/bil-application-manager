package com.yaman;

import com.yaman.bil.IBILManager;
import com.yaman.ui.IUIGenerator;
import com.yaman.ui.IUIUpdaterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import javax.annotation.PreDestroy;

@SpringBootApplication
public class BILApplicationManager implements CommandLineRunner {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BILApplicationManager.class).headless(false).run(args);
    }

    @Autowired
    IBILManager bilManager;

    @Autowired
    IUIGenerator uiGenerator;

    @Autowired
    IUIUpdaterService uiUpdaterService;

    @PreDestroy
    public void destroy() {
        bilManager.disconnect();
    }

    @Override
    public void run(String... args) {
        uiGenerator.initialize();
        bilManager.connect();
        uiUpdaterService.checkAndUpdateView();
    }
}
