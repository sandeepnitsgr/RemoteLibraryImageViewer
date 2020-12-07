package com.baghira.ui;

import com.baghira.downloader.DownloadListener;
import com.baghira.downloader.ImageDownloader;
import com.baghira.search.filesystem.EntriesFinder;
import com.baghira.util.CSVReader;
import com.baghira.util.UrlAndPathHelper;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RemoteImageDownloaderView extends JDialog implements DownloadListener {
    JPanel panel;
    private JList imageList;
    private JScrollPane scrollPane;
    private JLabel disclaimer;
    private final Project project;
    private List<Pair<String, String>> fileNameAndTypeList;
    private UrlAndPathHelper urlAndPathHelper;
    private List<ImageIcon> imageIconList;
    private String[] imageNames;

    public RemoteImageDownloaderView(Project project) {
        this.project = project;
        setUIComponentAttributes();
        setImageListAttributes();
        initUrlAndPathHelper();
        initEntriesFinder();
        initCsvReaderAndGetDistinctFileName();
        initImageIconAndName();
        createDownloadUrl();
        startImageDownloader();
    }

    private void initEntriesFinder() {
        EntriesFinder entriesFinder = new EntriesFinder(project.getBasePath());
        entriesFinder.getAllRelevantEntries();
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
        imageList.setBorder(JBUI.Borders.empty(5));
        imageList.setFixedCellWidth(190);
        imageList.setFixedCellHeight(170);
    }

    private void initCsvReaderAndGetDistinctFileName() {
        CSVReader reader = new CSVReader(urlAndPathHelper.getBasePath(), urlAndPathHelper.getRelativeCsvFilePath());
        reader.initReader();
        fileNameAndTypeList = reader.getDistinctFilesName();
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
