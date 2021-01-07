package com.baghira.ui.tabs;

import com.intellij.openapi.util.Pair;

import java.util.List;

public class AllCriteriaTab extends CriteriaTabsAbstract {
    List<String> fileNamesList;
    List<Pair<String, String>> allFileNameAndTypeList;

    public AllCriteriaTab(List<Pair<String, String>> allFileNameAndTypeList, List<String> fileNamesList) {
        super();
        this.fileNamesList = fileNamesList;
        this.allFileNameAndTypeList = allFileNameAndTypeList;
        initData();
    }

    @Override
    protected List<String> getFileLocationOfImages() {
        return fileNamesList;
    }

    @Override
    protected List<Pair<String, String>> getFileNameAndTypeList() {
        return allFileNameAndTypeList;
    }
}
