package com.baghira.ui.tabs;

import com.baghira.downloader.CsvUpdater;
import com.baghira.ui.RemoteImageDownloaderView;
import com.baghira.util.CriteriaTabType;
import com.intellij.openapi.util.Pair;

import java.util.List;

public class CriteriaTabFactory {
    public static CriteriaTabsAbstract getTab(List<String> fileNamesList, CriteriaTabType tabType, CsvUpdater callback) {
        switch (tabType) {
            case ALL:
                return new AllCriteriaTab(fileNamesList);
            case PRE_FETCHED:
                return new PreFetchedCriteriaTab(fileNamesList);
            case NON_PRE_FETCHED:
                return new NonPreFetchedCriteriaTab(fileNamesList, callback);
            case DOWNLOAD_FAILED:
                return new DownloadFailedCriteriaTab(fileNamesList);
        }
        return null;
    }
}
