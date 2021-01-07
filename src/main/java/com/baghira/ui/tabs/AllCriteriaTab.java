package com.baghira.ui.tabs;

import java.util.List;

public class AllCriteriaTab extends CriteriaTabsAbstract {
    List<String> fileNamesList;

    public AllCriteriaTab(List<String> fileNamesList) {
        super();
        this.fileNamesList = fileNamesList;
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
}
