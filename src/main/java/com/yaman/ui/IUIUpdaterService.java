package com.yaman.ui;

public interface IUIUpdaterService {
    void checkAndUpdateView();
    void startAndUpdateView();
    void stopAndUpdateView();
    void forceStopAndUpdateView();
}
