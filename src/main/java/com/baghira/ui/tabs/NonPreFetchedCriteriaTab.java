package com.baghira.ui.tabs;

import java.util.List;

public class NonPreFetchedCriteriaTab extends CriteriaTabsAbstract {

    private final List<String> fileNamesList;

    public NonPreFetchedCriteriaTab(List<String> fileNamesList) {
        super();
        this.fileNamesList = fileNamesList;
        initData();
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
}
