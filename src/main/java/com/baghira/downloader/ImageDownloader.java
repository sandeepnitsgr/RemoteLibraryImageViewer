package com.baghira.downloader;

import com.baghira.util.UrlAndPathHelper;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;

public class ImageDownloader implements PropertyChangeListener {
    private final UrlAndPathHelper urlAndPathHelper;
    private final DownloadListener callback;
    private JProgressBar progressBar;


    public ImageDownloader(UrlAndPathHelper urlAndPathHelper, DownloadListener callback, JProgressBar progressBar) {
        this.progressBar = progressBar;
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
        DownloadTask task = new DownloadTask(new WeakReference(urlAndPathHelper));
        task.addPropertyChangeListener(this);
        task.execute();
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName().equals("progress")) {
            int progress = (Integer) propertyChangeEvent.getNewValue();
            progressBar.setValue(progress);
            if (progress == 100) {
                progressBar.setVisible(false);
                callback.setIconsDetailInView();
            }
        }

    }
}
