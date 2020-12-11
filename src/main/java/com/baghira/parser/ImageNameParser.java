package com.baghira.parser;

import com.baghira.search.filesystem.EntriesFinder;
import com.baghira.search.filesystem.FileNameFinder;
import com.baghira.search.filesystem.JavaEntriesFileNameFinder;
import com.baghira.search.filesystem.XMLEntriesFileNameFinder;
import com.intellij.openapi.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class ImageNameParser {
    public static final String METHOD_LOAD_REMOTE_IMAGE_DRAWABLE = "\\.loadRemoteImageDrawable";
    public static final String TAG_REMOTE_FILE_NAME_ATTRIBUTE = "app:remoteFileName=";
    public static final String TAG_IMAGE_DPI_TYPE = "app:imageDpiSupportType=";

    private String basePath;
    private final HashSet<Pair<String, String>> resultSet;

    public ImageNameParser() {
        resultSet = new HashSet<>();
    }

    public void initEntriesSearch(String basePath) {
        this.basePath = basePath;
        processAndAddToResult(searchEntries(basePath,
                METHOD_LOAD_REMOTE_IMAGE_DRAWABLE),
                new JavaEntriesFileNameFinder());

        String densityTypeResult = searchEntries(basePath, TAG_IMAGE_DPI_TYPE);
        processAndAddToResult(searchEntries(basePath,
                TAG_REMOTE_FILE_NAME_ATTRIBUTE),
                new XMLEntriesFileNameFinder(densityTypeResult));
    }

    public HashSet<Pair<String, String>> getAllImagesName() {
        return resultSet;
    }

    private String searchEntries(String path, String searchString) {
        return EntriesFinder.searchEntry(path, searchString);
    }

    private void processAndAddToResult(String rawSearchResult, FileNameFinder fileNameFinder) {
        Set<Pair<String, String>> fileNameList = fileNameFinder.findAllFileNames(basePath, rawSearchResult);
        if (fileNameList != null)
            for (Pair<String, String> fileName : fileNameList)
                addToResult(fileName);
    }

    private void addToResult(Pair<String, String> processedResult) {
        resultSet.add(processedResult);
    }
}
