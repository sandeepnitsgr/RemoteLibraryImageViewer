package com.baghira.search.filesystem;

import com.intellij.openapi.util.Pair;

import java.util.HashSet;

public interface FileNameFinder {
    HashSet<Pair<String, String>> findAllFileNames(String basePath, String rawString);
}
