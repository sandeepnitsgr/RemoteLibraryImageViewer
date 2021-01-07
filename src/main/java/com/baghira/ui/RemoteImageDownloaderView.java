package com.baghira.ui;

import com.baghira.downloader.DownloadListener;
import com.baghira.downloader.ImageDownloader;
import com.baghira.parser.ImageNameParser;
import com.baghira.ui.tabs.CriteriaTabFactory;
import com.baghira.util.CSVReader;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RemoteImageDownloaderView extends JDialog implements DownloadListener {
    JPanel panel;
    private JLabel syncNow;
    private JLabel disclaimer;
    private JTabbedPane tabbedPane;
    private final Project project;
    CSVReader reader;

    HashSet<Pair<String, String>> allImagesNameAndType;
    List<String> allImagesNameList;
    List<String> preFetchedNameList;
    List<String> nonPreFetchedNameList;
    private UrlAndPathHelper urlAndPathHelper;
    private final Color greyBgColor = new Color(109, 107, 107, 223);
    private final Color originalBgColor = new Color(43, 43, 43);

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
            public void mouseClicked(MouseEvent e) {
                syncNow.setOpaque(true);
                syncNow.setForeground(JBColor.ORANGE);
                syncNow.setBackground(greyBgColor);
                System.out.println("clicked");
                SwingUtilities.invokeLater(() -> {
                    int index = tabbedPane.getSelectedIndex();
                    System.out.println("selected index = " + index);
                    init();
                    System.out.println(tabbedPane.getTabCount());
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
                syncNow.setBackground(originalBgColor);
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
        ImageNameParser entriesFinder = new ImageNameParser();
        entriesFinder.initEntriesSearch(project.getBasePath());
        allImagesNameAndType = entriesFinder.getAllImagesName();
    }

    private void initCsvReaderAndUpdateDistinctFileName() {
        reader = new CSVReader(urlAndPathHelper.getBasePath(), urlAndPathHelper.getRelativeCsvFilePath());
        reader.initReader();
        List<Pair<String, String>> distinctFilesName = reader.getDistinctFilesName();
        updateDistinctFileNameAndType(distinctFilesName);
    }

    private void createDownloadUrl() {
        for (Pair<String, String> fileNameAndType : allImagesNameAndType) {
            urlAndPathHelper.addToRemoteUrlList(fileNameAndType.second, fileNameAndType.first);
        }
    }

    private void startImageDownloader() {
        ImageDownloader imageDownloader = new ImageDownloader(urlAndPathHelper, this);
        imageDownloader.initDownload();
    }

    private void updateDistinctFileNameAndType(List<Pair<String, String>> distinctFilesName) {
        allImagesNameAndType.addAll(distinctFilesName);
    }

    private void initTabs() {
        int index = tabbedPane.getSelectedIndex();
        tabbedPane.removeAll();
        updateSuccessfulDownloadList();
        tabbedPane.addTab("ALL", CriteriaTabFactory.getTab(urlAndPathHelper.getLocalFilePathList(allImagesNameList), CriteriaTabType.ALL));
        tabbedPane.addTab("Pre-Fetched", CriteriaTabFactory.getTab(urlAndPathHelper.getLocalFilePathList(preFetchedNameList), CriteriaTabType.PRE_FETCHED));
        tabbedPane.addTab("Non Pre-Fetched", CriteriaTabFactory.getTab(urlAndPathHelper.getLocalFilePathList(nonPreFetchedNameList), CriteriaTabType.NON_PRE_FETCHED));
        tabbedPane.setSelectedIndex(index == -1 ? 0 : index);
    }

    private void updateSuccessfulDownloadList() {
        List<String> failedDownloadUrl = urlAndPathHelper.getFailedUrlList();
        List<String> failedDownloadNames = new ArrayList<>();

        for (String failedUrl : failedDownloadUrl) {
            failedDownloadNames.add(failedUrl.substring(failedUrl.lastIndexOf(File.separator) + 1));
        }
        allImagesNameList = new ArrayList<>();
        preFetchedNameList = new ArrayList<>();
        nonPreFetchedNameList = new ArrayList<>();
        for (Pair<String, String> nameAndType : allImagesNameAndType) {
            allImagesNameList.add(nameAndType.first);
        }
        System.out.println("failed: " + failedDownloadNames);
        allImagesNameList.removeAll(failedDownloadNames);
        List<Pair<String, String>> readerEntries = reader.getDistinctFilesName();
        readerEntries.removeIf(imageName -> failedDownloadNames.contains(imageName.first));
        for (Pair<String, String> nameAndType : readerEntries) {
            preFetchedNameList.add(nameAndType.first);
        }
        nonPreFetchedNameList = new ArrayList<>(allImagesNameList);
        nonPreFetchedNameList.removeAll(preFetchedNameList);

    }

    @Override
    public void setIconsDetailInView() {
        initTabs();
    }
}
