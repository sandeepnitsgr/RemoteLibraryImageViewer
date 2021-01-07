package com.baghira.ui.tabs;

import com.baghira.util.CriteriaTabType;
import com.intellij.openapi.util.Pair;

import java.util.List;

public class CriteriaTabFactory {
    public static CriteriaTabsAbstract getTab(List<Pair<String, String>> fileNameAndTypeList, List<String> filePath, CriteriaTabType tabType) {
        switch (tabType) {
            case ALL:
                return new AllCriteriaTab(fileNameAndTypeList, filePath);
            case PRE_FETCHED:
                return new PreFetchedCriteriaTab(fileNameAndTypeList, filePath);
            case NON_PRE_FETCHED:
                return new NonPreFetchedCriteriaTab(fileNameAndTypeList, filePath);
        }
        return null;
    }
}
