package com.baghira.downloader;

import java.util.List;

public interface CsvUpdater {
    void addToCsv(boolean shouldAddToCustomerapp, List<String> imageList);

    void showNotification(String message);
}
