package com.baghira.ui;

import com.baghira.downloader.DownloadListener;
import com.baghira.downloader.ImageDownloader;
import com.baghira.parser.ImageNameParser;
import com.baghira.ui.tabs.CriteriaTabFactory;
import com.baghira.ui.tabs.JButtonListDemo;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.baghira.ui.tabs.Main.createAndShowGUI;

public class RemoteImageDownloaderView extends JDialog implements DownloadListener {
    JPanel panel;
    private JLabel syncNow;
    private JLabel disclaimer;
    private JTabbedPane tabbedPane;
    private JPanel allTab;
    private JPanel preFetchTab;
    private JPanel nonPreFetchTab;
    private JList<String> imageList;
    private JList<String> preFetchImageList;
    private JList<String> nonPreFetchImageList;
    private final Project project;
    CSVReader reader;

    private List<Pair<String, String>> allFileNameAndTypeList;
    private List<Pair<String, String>> preFetchedFileNameAndTypeList;
    private List<Pair<String, String>> nonPreFetchedFileNameAndTypeList;
    HashSet<Pair<String, String>> allImagesName;
    HashSet<Pair<String, String>> preFetchedImagesName;
    private UrlAndPathHelper urlAndPathHelper;
    private List<ImageIcon> imageIconList;
    private String[] imageNames;
    private final Color greyBgColor = new Color(109, 107, 107, 223);
    private final Color originalBgColor = new Color(43, 43, 43);

    public RemoteImageDownloaderView(Project project) {
        this.project = project;
        setUIComponentAttributes();
        init();
    }

    private void init() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        initUrlAndPathHelper();
        initEntriesFinder();
        initCsvReaderAndUpdateDistinctFileName();
        initAllTabImageIconAndName();
        createDownloadUrl();
        startImageDownloader();
    }

    private void initTabs() {
        tabbedPane.removeAll();
        tabbedPane.addTab("ALL", CriteriaTabFactory.getTab(allFileNameAndTypeList, urlAndPathHelper.getLocalFilePathList(allFileNameAndTypeList), CriteriaTabType.ALL));
        tabbedPane.addTab("Pre-Fetched", CriteriaTabFactory.getTab(preFetchedFileNameAndTypeList, urlAndPathHelper.getLocalFilePathList(preFetchedFileNameAndTypeList), CriteriaTabType.PRE_FETCHED));
        tabbedPane.addTab("Non Pre-Fetched", CriteriaTabFactory.getTab(nonPreFetchedFileNameAndTypeList, urlAndPathHelper.getLocalFilePathList(nonPreFetchedFileNameAndTypeList), CriteriaTabType.NON_PRE_FETCHED));
    }

    private void initEntriesFinder() {
        ImageNameParser entriesFinder = new ImageNameParser();
        entriesFinder.initEntriesSearch(project.getBasePath());
        allImagesName = entriesFinder.getAllImagesName();
    }

    private void setUIComponentAttributes() {
        setImageListAttributes();
        setDisclaimerAttributes();
        setActionListener();
    }

    private void setActionListener() {
        syncNow.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                syncNow.setOpaque(true);
                syncNow.setForeground(greyBgColor);
                System.out.println("clicked");
                imageList.setVisible(false);
                SwingUtilities.invokeLater(() -> {
                    init();
                    imageList.setVisible(true);
                });
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                syncNow.setOpaque(true);
                syncNow.setBackground(greyBgColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                syncNow.setOpaque(false);
                syncNow.setBackground(originalBgColor);
            }
        });
    }

    private void setDisclaimerAttributes() {
        Font f = disclaimer.getFont();
        disclaimer.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        disclaimer.setForeground(JBColor.RED);
        Border border = disclaimer.getBorder();
        Border margin = JBUI.Borders.empty(5);
        disclaimer.setBorder(new CompoundBorder(border, margin));
    }

    private void initAllTabImageIconAndName() {
        imageIconList = new ArrayList<>();
        imageNames = new String[allFileNameAndTypeList.size()];
    }

    private void createDownloadUrl() {
        int pos = 0;
        for (Pair<String, String> fileNameAndType : allFileNameAndTypeList) {
            urlAndPathHelper.addToRemoteUrlList(fileNameAndType.second, fileNameAndType.first);
            addToImageNamesArray(pos, fileNameAndType.first);
            pos++;
        }
    }

    private void addToImageNamesArray(int pos, String fileName) {
        imageNames[pos] = fileName;
    }

    private void initUrlAndPathHelper() {
        urlAndPathHelper = new UrlAndPathHelper(project);
    }

    private void setImageListAttributes() {
        syncNow.setIcon(AllIcons.Javaee.UpdateRunningApplication);
        imageList.setBorder(JBUI.Borders.empty(20));
        imageList.setFixedCellWidth(190);
        imageList.setFixedCellHeight(170);
    }

    private void initCsvReaderAndUpdateDistinctFileName() {
        reader = new CSVReader(urlAndPathHelper.getBasePath(), urlAndPathHelper.getRelativeCsvFilePath());
        reader.initReader();
        List<Pair<String, String>> distinctFilesName = reader.getDistinctFilesName();
        updateDistinctFileNameForAllTab(distinctFilesName);
        updateDistinctFileNameForPreFetchedTab(distinctFilesName);
        updateDistinctFileNameForNonPreFetchedTab();
    }

    private void updateDistinctFileNameForAllTab(List<Pair<String, String>> distinctFilesName) {
        allImagesName.addAll(distinctFilesName);
        allFileNameAndTypeList = new ArrayList<>(allImagesName);
    }

    private void updateDistinctFileNameForPreFetchedTab(List<Pair<String, String>> distinctFilesName) {
        preFetchedImagesName = new HashSet<>(distinctFilesName);
        preFetchedFileNameAndTypeList = new ArrayList<>(preFetchedImagesName);
    }

    private void updateDistinctFileNameForNonPreFetchedTab() {
        HashSet<Pair<String, String>> nonPreFetchedName = new HashSet<>(allImagesName);
        nonPreFetchedName.removeAll(preFetchedImagesName);
        nonPreFetchedFileNameAndTypeList = new ArrayList<>(nonPreFetchedName);
    }

    private void startImageDownloader() {
        ImageDownloader imageDownloader = new ImageDownloader(urlAndPathHelper, this);
        imageDownloader.initDownload();
    }

    private void updateImageIconList() {
        List<String> localFileLocationList = urlAndPathHelper.getLocalFilePathList(allFileNameAndTypeList);
        for (String path : localFileLocationList) {
            imageIconList.add(getImageIcon(path));
        }
    }

    private ImageIcon getImageIcon(String path) {
        ImageIcon imageIcon = new ImageIcon(path);
        Image image = imageIcon.getImage();
        Image newImg = image.getScaledInstance(200, 160, Image.SCALE_DEFAULT);
        imageIcon = new ImageIcon(newImg);
        return imageIcon;
    }

    @Override
    public void setIconsDetailInView() {
        initTabs();
//        updateImageIconList();
//        imageList.setListData(imageNames);
//        imageList.setCellRenderer(new DownloadedImageCellRenderer(imageIconList));
    }
}
