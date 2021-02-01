package com.baghira.downloader;

import com.baghira.util.UrlAndPathHelper;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

public class DownloadTask extends SwingWorker<Void, Void> {
    private final UrlAndPathHelper urlAndPathHelper;

    public DownloadTask(WeakReference<UrlAndPathHelper> urlAndPathHelper) {
        this.urlAndPathHelper = urlAndPathHelper.get();
    }

    @Override
    protected Void doInBackground() {
        downloadFiles();
        return null;
    }

    private void downloadFiles() {
        if(urlAndPathHelper == null) return;
        List<String> remoteUrlList = urlAndPathHelper.getRemoteUrlList();
        int len = remoteUrlList.size();
        int count = 0;
        for (String url : remoteUrlList) {
            downloadFromUrl(url);
            count++;
            setProgress((count * 100 / len));
        }
    }


    private void downloadFromUrl(String url) {
        String imageName = url.substring(url.lastIndexOf(File.separator) + 1);
        String localFileLocation = urlAndPathHelper.getLocalFileBasePath() + imageName;
        downloadUsingStream(url, localFileLocation);
    }

    private void downloadUsingStream(String urlStr, String fileLoc) {
        File f = new File(fileLoc);
        if (!f.exists() || !urlAndPathHelper.getSuccessfulDownloadedImages().contains(urlStr)) {
            try {
                downloadAndSaveStream(urlStr, fileLoc, f);
            } catch (UnknownHostException exception) {
                exception.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                addFailedUrlToFailedList(urlStr);
                return;
            }
        }
        addLocalFileLocationToList(urlStr);
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

    private void addLocalFileLocationToList(String url) {
        urlAndPathHelper.addToDownloadSuccessList(url);
    }

    @Override
    protected void done() {
        if (!isCancelled()) {
        }
    }
}
