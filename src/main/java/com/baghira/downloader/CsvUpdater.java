package com.baghira.downloader;

import com.intellij.openapi.util.Pair;
import com.intellij.ui.components.JBList;

import java.util.List;

public interface CsvUpdater {
    void addToCsv(List<String> imageList, List<Pair<String, String>> fileNameAndTypeList);
}
