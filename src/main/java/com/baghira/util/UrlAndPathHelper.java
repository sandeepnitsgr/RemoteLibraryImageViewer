package com.baghira.util;

import com.intellij.openapi.project.Project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class UrlAndPathHelper {
    private static final String REMOTE_URL_BASE_PATH = "https://ecs7.tokopedia.net/android/res/";
    public static final String IMAGE_VIEWER = "imageviewer";
    public static final String GRADLE_LOCATION = ".gradle";
    public static final String URL_SEPARATOR = "/";
    private final TreeSet<String> remoteUrlList;
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
    private final List<String> successfulDownloadedImageList;
    private final HashSet<String> successfulDownloadedImages;
    private final List<String> failedUrlList;

    public UrlAndPathHelper(Project project) {
        this.project = project;
        remoteUrlList = new TreeSet<>();
        successfulDownloadedImageList = new ArrayList<>();
        successfulDownloadedImages = new HashSet<>();
        failedUrlList = new ArrayList<>();
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
        for (String downloaded : successfulDownloadedImageList) {
            remoteUrlList.removeIf(url -> url.contains(downloaded));
        }
        return new ArrayList<>(remoteUrlList);
    }

    public List<String> getLocalFilePathList(List<String> fileNameAndTypeList) {
        List<String> list = new ArrayList<>();
        for (String fileNameTypePair : fileNameAndTypeList) {
            list.add(getLocalFileBasePath() + File.separator + fileNameTypePair);
        }
        return list;
    }

    public List<String> getFailedUrlList() {
        return failedUrlList;
    }

    public List<String> getSuccessfulDownloadedImageList() {
        return successfulDownloadedImageList;
    }

    public HashSet<String> getSuccessfulDownloadedImages() {
        return successfulDownloadedImages;
    }

    public void addToDownloadSuccessList(String localFileLocation, String url) {
        successfulDownloadedImageList.add(localFileLocation.substring(localFileLocation.lastIndexOf(File.separator) + 1));
        successfulDownloadedImages.add(url);
    }

    public void addToFailedUrlList(String urlStr) {
        failedUrlList.add(urlStr);
    }
}
