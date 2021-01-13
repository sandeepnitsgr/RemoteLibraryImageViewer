package com.baghira.downloader;

import com.baghira.util.UrlAndPathHelper;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ImageDownloader {
    private final UrlAndPathHelper urlAndPathHelper;
    private final DownloadListener callback;


    public ImageDownloader(UrlAndPathHelper urlAndPathHelper, DownloadListener callback) {
        this.urlAndPathHelper = urlAndPathHelper;
        this.callback = callback;
    }

    public void initDownload() {
        Runnable doDownloading = () -> {
            try {
                downloadFiles();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        SwingUtilities.invokeLater(doDownloading);
    }

    private void downloadFiles() {
        List<String> remoteUrlList = urlAndPathHelper.getRemoteUrlList();
        for (String url : remoteUrlList) {
            download(url);
        }
        callback.setIconsDetailInView();
    }

    private void download(String url) {
        String imageName = url.substring(url.lastIndexOf(File.separator) + 1);
        String localFileLocation = urlAndPathHelper.getLocalFileBasePath() + imageName;
        downloadUsingStream(url, localFileLocation);
    }

    private void downloadUsingStream(String urlStr, String file) {
        File f = new File(file);
        if (!f.exists()) {
            try {
                downloadAndSaveStream(urlStr, file, f);
            } catch (Exception e) {
                e.printStackTrace();
                addFailedUrlToFailedList(urlStr);
                return;
            }
        }
        addLocalFileLocationToList(file);
    }

    private void downloadAndSaveStream(String urlStr, String file, File f) throws IOException {
        URL url = new URL(urlStr);
        BufferedInputStream bis = new BufferedInputStream(url.openStream());
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        f.createNewFile();
        FileOutputStream fis = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int count;
        while ((count = bis.read(buffer, 0, 1024)) != -1) {
            fis.write(buffer, 0, count);
        }
        fis.close();
        bis.close();
    }

    private void addFailedUrlToFailedList(String urlStr) {
        urlAndPathHelper.addToFailedUrlList(urlStr);
    }

    private void addLocalFileLocationToList(String localFileLocation) {
        urlAndPathHelper.addToDownloadSuccessList(localFileLocation);
    }

}
