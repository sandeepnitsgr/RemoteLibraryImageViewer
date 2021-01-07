package com.baghira.ui.tabs;


import com.baghira.ui.DownloadedImageCellRenderer;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CriteriaTabsAbstract extends JPanel {
    JBScrollPane scrollPane;
    JBList<String> imageList;
    private List<ImageIcon> imageIconList;
    private String[] imageNames;

    public CriteriaTabsAbstract() {
        imageList = new JBList<>();
        scrollPane = new JBScrollPane(imageList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    public void initData() {
        initAllTabImageIconAndName();
        updateImageIconList();
        imageList.setListData(imageNames);
        imageList.setCellRenderer(new DownloadedImageCellRenderer(imageIconList));
    }

    private void updateImageIconList() {
        List<String> localFileLocationList = getFileLocationOfImages();
        for (String path : localFileLocationList) {
            imageIconList.add(getImageIcon(path));
        }
    }

    protected abstract List<String> getFileLocationOfImages();

    private void initAllTabImageIconAndName() {
        imageIconList = new ArrayList<>();
        List<Pair<String, String>> list = getFileNameAndTypeList();
        imageNames = new String[list.size()];
        int count = 0;
        for (Pair<String, String> fileNameAndType : getFileNameAndTypeList()) {
            imageNames[count++] = fileNameAndType.first;
        }
    }

    protected abstract List<Pair<String, String>> getFileNameAndTypeList();

    private ImageIcon getImageIcon(String path) {
        ImageIcon imageIcon = new ImageIcon(path);
        Image image = imageIcon.getImage();
        Image newImg = image.getScaledInstance(200, 160, Image.SCALE_DEFAULT);
        imageIcon = new ImageIcon(newImg);
        return imageIcon;
    }
}
