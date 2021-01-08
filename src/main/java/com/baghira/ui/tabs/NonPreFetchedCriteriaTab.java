package com.baghira.ui.tabs;

import com.baghira.downloader.CsvUpdater;
import com.baghira.ui.RemoteImageDownloaderView;
import com.intellij.openapi.util.Pair;

import java.util.List;

public class NonPreFetchedCriteriaTab extends CriteriaTabsAbstract {

    private final List<String> fileNamesList;
    private List<Pair<String, String>> fileNameAndTypeList;

    private CsvUpdater callBack;

    public NonPreFetchedCriteriaTab(List<Pair<String, String>> nonPreFetchedFileNameAndTypeList, List<String> fileNamesList, RemoteImageDownloaderView remoteImageDownloaderView) {
        super();
        callBack = remoteImageDownloaderView;
        this.fileNameAndTypeList = nonPreFetchedFileNameAndTypeList;
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
        return true;
    }

    @Override
    protected List<String> getFileNameList() {
        return fileNamesList;
    }

    @Override
    protected List<Pair<String, String>> getFileNameAndTypeList() {
        return fileNameAndTypeList;
    }
}
