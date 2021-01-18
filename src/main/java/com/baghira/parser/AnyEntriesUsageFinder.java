package com.baghira.parser;

import com.baghira.search.filesystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class AnyEntriesUsageFinder {
    public static final String METHOD_LOAD_REMOTE_IMAGE_DRAWABLE = "\\.loadRemoteImageDrawable";
    public static final String TAG_REMOTE_FILE_NAME_ATTRIBUTE = "app:remoteFileName=";
    private static final String CSV_USAGE_STRING = "resources_description";

    private String basePath;
    private Set<Pair<String, String>> resultSet;
    private List<String> csvModulesNameList;
    private Project project;

    public AnyEntriesUsageFinder(Project project) {
        this.project = project;
        resultSet = new TreeSet<>(Comparator.comparing(o -> o.first));
    }

    public void initEntriesSearch(String basePath) {
        this.basePath = basePath;
        processAndAddToResult(searchEntries(basePath,
                METHOD_LOAD_REMOTE_IMAGE_DRAWABLE),
                new JavaEntriesFileNameFinder());

        processAndAddToResult(searchEntries(basePath,
                TAG_REMOTE_FILE_NAME_ATTRIBUTE),
                new XMLEntriesFileNameFinder(project));

        processCsvModules(searchEntries(basePath, CSV_USAGE_STRING), new CSVUsageFileNameFinder());
    }

    private void processCsvModules(String searchEntries, FileNameFinder<List<String>> fileNameFinder) {
        csvModulesNameList = fileNameFinder.findAllFileNames(basePath, searchEntries);
    }

    public Set<Pair<String, String>> getAllImagesName() {
        return resultSet;
    }

    private String searchEntries(String path, String searchString) {
        return EntriesFinder.searchEntry(path, searchString);
    }

    private void processAndAddToResult(String rawSearchResult, FileNameFinder<Set<Pair<String, String>>> fileNameFinder) {
        Set<Pair<String, String>> entries = fileNameFinder.findAllFileNames(basePath, rawSearchResult);
        resultSet.addAll(entries);
    }

    public List<String> getCsvUsageModulesNameList() {
        return csvModulesNameList;
    }
}
