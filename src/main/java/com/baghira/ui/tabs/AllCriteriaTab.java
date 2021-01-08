package com.baghira.ui.tabs;

import com.intellij.openapi.util.Pair;

import java.util.List;

public class AllCriteriaTab extends CriteriaTabsAbstract {
    List<String> fileNamesList;
    List<Pair<String, String>> allFileNameAndTypeList;

    public AllCriteriaTab(List<Pair<String, String>> fileNameAndTypeList, List<String> fileNamesList) {
        super();
        this.fileNamesList = fileNamesList;
        this.allFileNameAndTypeList = fileNameAndTypeList;
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
        return allFileNameAndTypeList;
    }
}
