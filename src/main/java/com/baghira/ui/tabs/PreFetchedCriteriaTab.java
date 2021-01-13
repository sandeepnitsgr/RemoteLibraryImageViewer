package com.baghira.ui.tabs;

import com.intellij.openapi.util.Pair;

import java.util.List;

public class PreFetchedCriteriaTab extends CriteriaTabsAbstract {
    private final List<String> fileNamesList;


    public PreFetchedCriteriaTab(List<String> fileNamesList) {
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
    protected String getAdditionalHeaderText() {
        return null;
    }

    @Override
    protected List<String> getFileNameList() {
        return fileNamesList;
    }

}
