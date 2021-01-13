package com.baghira.search.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CSVUsageFileNameFinder implements FileNameFinder<List<String>> {
    @Override
    public List<String> findAllFileNames(String basePath, String rawString) {
        List<String> nameList = new ArrayList<>();
        String[] allOutputLines = rawString.split("\\n");
        for (String outputLine : allOutputLines) {
            if(!outputLine.isEmpty())
            nameList.add(outputLine.substring(0, outputLine.indexOf(File.separator)));
        }
        return nameList;
    }
}
