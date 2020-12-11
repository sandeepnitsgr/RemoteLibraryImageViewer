package com.baghira.ui;

import com.baghira.downloader.DownloadListener;
import com.baghira.downloader.ImageDownloader;
import com.baghira.parser.ImageNameParser;
import com.baghira.util.CSVReader;
import com.baghira.util.UrlAndPathHelper;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class RemoteImageDownloaderView extends JDialog implements DownloadListener {
    JPanel panel;
    private JList imageList;
    private JScrollPane scrollPane;
    private JTabbedPane tabbedPane1;
    private JLabel syncNow;
    private JLabel disclaimer;
    private final Project project;
    private List<Pair<String, String>> fileNameAndTypeList;
    HashSet<Pair<String, String>> allImagesName;
    private UrlAndPathHelper urlAndPathHelper;
    private List<ImageIcon> imageIconList;
    private String[] imageNames;

    public RemoteImageDownloaderView(Project project) {
        this.project = project;
        setUIComponentAttributes();
        setImageListAttributes();
        initUrlAndPathHelper();
        initEntriesFinder();
        initCsvReaderAndUpdateDistinctFileName();
        initImageIconAndName();
        createDownloadUrl();
        startImageDownloader();
    }

    private void initEntriesFinder() {
        ImageNameParser entriesFinder = new ImageNameParser();
        entriesFinder.initEntriesSearch(project.getBasePath());
        allImagesName = entriesFinder.getAllImagesName();
    }

    private void setUIComponentAttributes() {
        setImageListAttributes();
        setDisclaimerAttributes();
    }

    private void setDisclaimerAttributes() {
        Font f = disclaimer.getFont();
        disclaimer.setFont(f.deriveFont(f.getStyle() | Font.BOLD));
        disclaimer.setForeground(JBColor.RED);
        Border border = disclaimer.getBorder();
        Border margin = JBUI.Borders.empty(5);
        disclaimer.setBorder(new CompoundBorder(border, margin));
    }

    private void initImageIconAndName() {
        imageIconList = new ArrayList<>();
        imageNames = new String[fileNameAndTypeList.size()];

    }

    private void createDownloadUrl() {
        int pos = 0;
        for (Pair<String, String> fileNameAndType : fileNameAndTypeList) {
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
        imageList.setBorder(JBUI.Borders.empty(5));
        imageList.setFixedCellWidth(190);
        imageList.setFixedCellHeight(170);
    }

    private void initCsvReaderAndUpdateDistinctFileName() {
        CSVReader reader = new CSVReader(urlAndPathHelper.getBasePath(), urlAndPathHelper.getRelativeCsvFilePath());
        reader.initReader();
        List<Pair<String, String>> distinctFilesName = reader.getDistinctFilesName();
        updateDistinctFileName(distinctFilesName);
    }

    private void updateDistinctFileName(List<Pair<String, String>> distinctFilesName) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(EntriesFinder.searchEntry(project.getBasePath(), "\\.loadRemoteImageDrawable")).append("\n****\n");
//        sb.append(EntriesFinder.searchEntry(project.getBasePath(), "app:remoteFileName=")).append("\n****\n");
//        int count = 1;
////        AllIcons.Javaee.UpdateRunningApplication
//        for (Pair<String, String> e : allImagesName) {
//            sb.append(count).append(". ").append(e.first).append(" : ").append(e.second).append("\n");
//            count++;
//        }
//        for (Pair<String, String> ab : allImagesName)
//            System.out.println(ab);
//        System.out.println(distinctFilesName);
//        output.setText(sb.toString());
        allImagesName.addAll(distinctFilesName);
        fileNameAndTypeList = new ArrayList<>(allImagesName);
    }

    private void startImageDownloader() {
        ImageDownloader imageDownloader = new ImageDownloader(urlAndPathHelper, this);
        imageDownloader.initDownload();
    }

    private void updateImageIconList() {
        List<String> localFileLocationList = urlAndPathHelper.getLocalFilePathList();

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
        updateImageIconList();
        imageList.setListData(imageNames);
        imageList.setCellRenderer(new DownloadedImageCellRenderer(imageIconList));
    }
}
