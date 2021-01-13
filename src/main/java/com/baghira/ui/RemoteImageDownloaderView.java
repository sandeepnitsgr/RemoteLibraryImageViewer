package com.baghira.ui;

import com.baghira.downloader.CsvUpdater;
import com.baghira.downloader.DownloadListener;
import com.baghira.downloader.ImageDownloader;
import com.baghira.parser.AnyEntriesUsageFinder;
import com.baghira.ui.notification.NotificationsHelper;
import com.baghira.ui.tabs.CriteriaTabFactory;
import com.baghira.util.CSVReaderAndWriter;
import com.baghira.util.CriteriaTabType;
import com.baghira.util.UrlAndPathHelper;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.*;

public class RemoteImageDownloaderView extends JDialog implements DownloadListener, CsvUpdater {
    JPanel panel;
    private JLabel syncNow;
    private JLabel disclaimer;
    private JTabbedPane tabbedPane;
    private final Project project;
    private CSVReaderAndWriter reader;
    private AnyEntriesUsageFinder entriesFinder;

    Set<Pair<String, String>> imageToDownloadSet;

    HashMap<String, Pair<String, String>> fileNameAndTypeMap;

    List<String> successfulDownloadAllList;
    List<String> successfulPreFetchedList;
    List<String> successfulNonPreFetchedList;
    List<String> downloadFailedList;


    private UrlAndPathHelper urlAndPathHelper;
    private final Color greyBgColor = new Color(109, 107, 107, 223);

    public RemoteImageDownloaderView(Project project) {
        this.project = project;
        setUIComponentAttributes();
        init();
    }

    private void setUIComponentAttributes() {
        setImageListAttributes();
        setDisclaimerAttributes();
        setActionListener();
    }

    private void setImageListAttributes() {
        syncNow.setIcon(AllIcons.Javaee.UpdateRunningApplication);
        syncNow.setOpaque(true);
        syncNow.setForeground(JBColor.BLUE);
    }

    private void setDisclaimerAttributes() {
        Font f = disclaimer.getFont();
        disclaimer.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        disclaimer.setForeground(JBColor.RED);
        Border border = disclaimer.getBorder();
        Border margin = JBUI.Borders.empty(5);
        disclaimer.setBorder(new CompoundBorder(border, margin));
    }

    private void setActionListener() {
        syncNow.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                syncNow.setForeground(JBColor.ORANGE);
                syncNow.setBackground(greyBgColor);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                syncNow.setForeground(JBColor.PINK);
                syncNow.setBackground(UIManager.getColor("control"));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                syncNow.setOpaque(true);
                SwingUtilities.invokeLater(() -> {
                    init();
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                syncNow.setOpaque(true);
                syncNow.setForeground(JBColor.PINK);
                syncNow.setBackground(greyBgColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                syncNow.setOpaque(false);
                syncNow.setForeground(JBColor.BLUE);
                syncNow.setBackground(UIManager.getColor("control"));
            }
        });
    }

    private void init() {
        initUrlAndPathHelper();
        initEntriesFinder();
        initCsvReaderAndUpdateDistinctFileName();
        createDownloadUrl();
        startImageDownloader();
    }

    private void initUrlAndPathHelper() {
        urlAndPathHelper = new UrlAndPathHelper(project);
    }

    private void initEntriesFinder() {
        entriesFinder = new AnyEntriesUsageFinder();
        entriesFinder.initEntriesSearch(project.getBasePath());
        imageToDownloadSet = entriesFinder.getAllImagesName();
    }

    private void initCsvReaderAndUpdateDistinctFileName() {
        reader = new CSVReaderAndWriter(urlAndPathHelper.getBasePath(), urlAndPathHelper.getRelativeCsvFilePath());
        reader.initReader(entriesFinder.getCsvUsageModulesNameList());
        Set<Pair<String, String>> distinctFilesName = reader.getDistinctFilesName();
        for (Pair<String, String> distinctFile : distinctFilesName) {
            imageToDownloadSet.removeIf(imageToDownload -> imageToDownload.first.equals(distinctFile.first));
        }
        updateDistinctFileName(distinctFilesName);
    }

    private void updateDistinctFileName(Set<Pair<String, String>> distinctFilesName) {
        imageToDownloadSet.addAll(distinctFilesName);
    }

    private void createDownloadUrl() {
        fileNameAndTypeMap = new HashMap<>();
        for (Pair<String, String> fileNameAndType : imageToDownloadSet) {
            urlAndPathHelper.addToRemoteUrlList(fileNameAndType.second, fileNameAndType.first);
            fileNameAndTypeMap.put(fileNameAndType.first, fileNameAndType);
        }
    }

    private void startImageDownloader() {
        ImageDownloader imageDownloader = new ImageDownloader(urlAndPathHelper, this);
        imageDownloader.initDownload();
    }

    private void initTabs() {
        int index = tabbedPane.getSelectedIndex();
        tabbedPane.removeAll();
        updateSuccessfulDownloadList();
        tabbedPane.addTab("ALL", CriteriaTabFactory.getTab(urlAndPathHelper.getLocalFilePathList(successfulDownloadAllList), CriteriaTabType.ALL, this));
        tabbedPane.addTab("Pre-Fetched", CriteriaTabFactory.getTab(urlAndPathHelper.getLocalFilePathList(successfulPreFetchedList), CriteriaTabType.PRE_FETCHED, this));
        tabbedPane.addTab("Non Pre-Fetched", CriteriaTabFactory.getTab(urlAndPathHelper.getLocalFilePathList(successfulNonPreFetchedList), CriteriaTabType.NON_PRE_FETCHED, this));
        if (urlAndPathHelper.getFailedUrlList().size() > 0)
            tabbedPane.addTab("Download Failed", CriteriaTabFactory.getTab(urlAndPathHelper.getLocalFilePathList(downloadFailedList), CriteriaTabType.DOWNLOAD_FAILED, this));
        tabbedPane.setSelectedIndex(index == -1 ? 0 : (index < tabbedPane.getTabCount()? index : 0));
    }

    private void updateSuccessfulDownloadList() {
        List<String> failedDownloadUrl = urlAndPathHelper.getFailedUrlList();
        downloadFailedList = new ArrayList<>();

        for (String failedUrl : failedDownloadUrl) {
            downloadFailedList.add(failedUrl.substring(failedUrl.lastIndexOf(File.separator) + 1));
        }
        successfulDownloadAllList = urlAndPathHelper.getSuccessfulDownloadedImageList();
        Set<Pair<String, String>> distinctFilesNameList = reader.getDistinctFilesName();

        distinctFilesNameList.removeIf(distinctFiles -> downloadFailedList.contains(distinctFiles.first));
        successfulPreFetchedList = new ArrayList<>();

        for (Pair<String, String> entry : distinctFilesNameList) {
            successfulPreFetchedList.add(entry.first);
        }

        successfulNonPreFetchedList = new ArrayList<>(successfulDownloadAllList);

        successfulNonPreFetchedList.removeIf(entry -> successfulPreFetchedList.contains(entry));
    }

    @Override
    public void setIconsDetailInView() {
        initTabs();
    }

    @Override
    public void addToCsv(boolean shouldAddToCustomerapp, List<String> imageList) {
        List<Pair<String, String>> finalList = new ArrayList<>();
        List<String> names = new ArrayList<>();

        for (String name : imageList) {
            if (fileNameAndTypeMap.containsKey(name)) {
                Pair<String, String> nameType = fileNameAndTypeMap.get(name);
                finalList.add(nameType);
                names.add(nameType.first);
            }
        }

        reader.writeToCSV(shouldAddToCustomerapp, finalList);
        updateList(names);
    }

    @Override
    public void showNotification(String message) {
        NotificationsHelper.showNotification(project, message);
    }

    private void updateList(List<String> imageList) {
        updateLists(imageList);
        init();
    }

    private void updateLists(List<String> imageList) {
        successfulPreFetchedList.addAll(imageList);
        successfulNonPreFetchedList.removeAll(imageList);
    }
}
