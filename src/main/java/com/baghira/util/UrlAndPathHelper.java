package com.baghira.util;

import com.intellij.openapi.project.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UrlAndPathHelper {
    private static final String REMOTE_URL_BASE_PATH = "https://ecs7.tokopedia.net/android/res/";
    public static final String IMAGE_VIEWER = "imageviewer";
    public static final String GRADLE_LOCATION = ".gradle";
    public static final String URL_SEPARATOR = "/";
    private List<String> remoteUrlList;
    private static final String RELATIVE_PATH = File.separator +
            "src" +
            File.separator +
            "main" +
            File.separator +
            "res" +
            File.separator +
            "raw" +
            File.separator +
            "resources_description.csv";
    private final Project project;
    private List<String> localFilePathList;

    public UrlAndPathHelper(Project project) {
        this.project = project;
        remoteUrlList = new ArrayList<>();
        localFilePathList = new ArrayList<>();
    }

    public String getRelativeCsvFilePath() {
        return RELATIVE_PATH;
    }

    public String getBasePath() {
        return project.getBasePath() + File.separator;
    }

    public String getLocalFileBasePath() {
        return getBasePath() +
                getTempGradleFolderName() +
                File.separator +
                getDownloadLocationName() +
                File.separator;
    }

    private String getDownloadLocationName() {
        return IMAGE_VIEWER;
    }

    private String getTempGradleFolderName() {
        return GRADLE_LOCATION;
    }

    public void addToRemoteUrlList(String relativePath, String fileName) {
        remoteUrlList.add(REMOTE_URL_BASE_PATH + relativePath + URL_SEPARATOR + fileName);
    }

    public List<String> getRemoteUrlList() {
        return remoteUrlList;
    }

    public List<String> getLocalFilePathList() {
        return localFilePathList;
    }

    public void addToLocalFileLocationList(String localFileLocation) {
        localFilePathList.add(localFileLocation);
    }
}
