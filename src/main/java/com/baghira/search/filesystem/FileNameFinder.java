package com.baghira.search.filesystem;

public interface FileNameFinder<T> {
    T findAllFileNames(String basePath, String rawString);
}
