package com.yaman.ui;

import org.springframework.stereotype.Component;

@Component
public interface IUIGenerator {
   void initialize();
   void viewModeStarted();
   void viewModeStopped();
   void viewModeForcedStop();
}
