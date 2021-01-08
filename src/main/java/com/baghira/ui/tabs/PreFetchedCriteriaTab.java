package com.baghira.ui.tabs;

import com.intellij.openapi.util.Pair;

import java.util.List;

public class PreFetchedCriteriaTab extends CriteriaTabsAbstract {
    private final List<String> fileNamesList;
    List<Pair<String, String>> allPreFetchedFileNameAndTypeList;


    public PreFetchedCriteriaTab(List<Pair<String, String>> fileNameAndTypeList, List<String> fileNamesList) {
        super();
        this.fileNamesList = fileNamesList;
        this.allPreFetchedFileNameAndTypeList = fileNameAndTypeList;
        initData();
    }

    @Override
    protected boolean shouldEnableSelection() {
        return false;
    }

    @Override
    protected boolean getButtonVisibility() {
        return false;
    }

    @Override
    protected List<String> getFileNameList() {
        return fileNamesList;
    }

    @Override
    protected List<Pair<String, String>> getFileNameAndTypeList() {
        return allPreFetchedFileNameAndTypeList;
    }
}
