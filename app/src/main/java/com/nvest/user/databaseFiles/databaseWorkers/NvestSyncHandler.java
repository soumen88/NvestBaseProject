package com.nvest.user.databaseFiles.databaseWorkers;

public interface NvestSyncHandler {
    void notifyStatus(String taskName, boolean completed);
}
