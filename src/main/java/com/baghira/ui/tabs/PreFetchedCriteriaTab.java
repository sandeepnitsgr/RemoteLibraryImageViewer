package com.baghira.ui.tabs;

import com.intellij.openapi.util.Pair;

import java.util.List;

public class PreFetchedCriteriaTab extends CriteriaTabsAbstract {
    private final List<String> fileNamesList;
    private final List<Pair<String, String>> preFetchedFileNameAndTypeList;

    public PreFetchedCriteriaTab(List<Pair<String, String>> preFetchedFileNameAndTypeList, List<String> fileNamesList) {
        super();
        this.fileNamesList = fileNamesList;
        this.preFetchedFileNameAndTypeList = preFetchedFileNameAndTypeList;
        initData();
    }

    @Override
    protected List<String> getFileLocationOfImages() {
        return fileNamesList;
    }

    @Override
    protected List<Pair<String, String>> getFileNameAndTypeList() {
        return preFetchedFileNameAndTypeList;
    }
}
