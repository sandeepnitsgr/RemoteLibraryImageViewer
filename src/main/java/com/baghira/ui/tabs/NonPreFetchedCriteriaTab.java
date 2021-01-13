package com.baghira.ui.tabs;

import com.baghira.downloader.CsvUpdater;
import com.baghira.ui.RemoteImageDownloaderView;
import com.intellij.openapi.util.Pair;

import java.util.List;

public class NonPreFetchedCriteriaTab extends CriteriaTabsAbstract {

    private final List<String> fileNamesList;

    public NonPreFetchedCriteriaTab(List<String> fileNamesList, CsvUpdater callBack) {
        super();
        this.fileNamesList = fileNamesList;
        initData();
        setCallBack(callBack);
    }

    @Override
    protected boolean shouldEnableSelection() {
        return true;
    }

    @Override
    protected boolean getButtonVisibility() {
        return fileNamesList.size() > 0;
    }

    @Override
    protected String getAdditionalHeaderText() {
        return null;
    }

    @Override
    protected List<String> getFileNameList() {
        return fileNamesList;
    }
}
