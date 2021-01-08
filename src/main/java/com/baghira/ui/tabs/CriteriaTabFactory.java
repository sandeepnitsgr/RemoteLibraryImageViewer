package com.baghira.ui.tabs;

import com.baghira.ui.RemoteImageDownloaderView;
import com.baghira.util.CriteriaTabType;
import com.intellij.openapi.util.Pair;

import java.util.List;

public class CriteriaTabFactory {
    public static CriteriaTabsAbstract getTab(List<Pair<String, String>> fileNameAndTypeList, List<String> fileNamesList, CriteriaTabType tabType, RemoteImageDownloaderView remoteImageDownloaderView) {
        switch (tabType) {
            case ALL:
                return new AllCriteriaTab(fileNameAndTypeList, fileNamesList);
            case PRE_FETCHED:
                return new PreFetchedCriteriaTab(fileNameAndTypeList, fileNamesList);
            case NON_PRE_FETCHED:
                return new NonPreFetchedCriteriaTab(fileNameAndTypeList, fileNamesList, remoteImageDownloaderView);
        }
        return null;
    }
}
