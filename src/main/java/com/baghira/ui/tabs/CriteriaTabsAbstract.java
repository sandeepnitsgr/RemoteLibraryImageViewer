package com.baghira.ui.tabs;


import com.baghira.downloader.CsvUpdater;
import com.baghira.ui.DownloadedImageCellRenderer;
import com.baghira.util.CSVReader;
import com.baghira.util.UrlAndPathHelper;
import com.intellij.openapi.util.Pair;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class CriteriaTabsAbstract extends JPanel {
    JBScrollPane scrollPane;
    JBList<String> imageList;
    JButton button;
    JLabel numOfImages;
    private List<ImageIcon> imageIconList;
    private String[] imageNames;
    CSVReader reader;
    private UrlAndPathHelper urlAndPathHelper;
    private CsvUpdater callBack;


    public CriteriaTabsAbstract() {
        imageList = new JBList<>();
        imageList.setBorder(JBUI.Borders.empty(5));
        imageList.setFixedCellWidth(190);
        imageList.setFixedCellHeight(170);
        scrollPane = new JBScrollPane(imageList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button = new JButton("Add To CSV");
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(JBColor.CYAN, 5),
                BorderFactory.createEmptyBorder(5, 5, 10, 10)));
        button.setVisible(getButtonVisibility());
        setLayout(new BorderLayout());
        numOfImages = new JLabel();
        add(numOfImages, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
        button.addActionListener(new AddToVCSListener());
    }

    protected abstract boolean shouldEnableSelection();

    protected abstract boolean getButtonVisibility();

    public void initData() {
        initAllTabImageIconAndName();
        updateImageIconList();
        imageList.setListData(imageNames);
        imageList.setCellRenderer(new DownloadedImageCellRenderer(imageIconList));
        numOfImages.setText(imageNames.length + " Images");
        numOfImages.setVisible(true);
        ListSelectionListener listener = null;
        if (shouldEnableSelection()) {
            imageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            imageList.addListSelectionListener(listSelectionEvent -> {
                int[] selectedIndexes = imageList.getSelectedIndices();
                System.out.println(Arrays.toString(selectedIndexes));
                for (int index : selectedIndexes) {
                    imageList.getModel().getElementAt(index);
                }
            });
        } else {
            imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
    }

    public void setCallBack(CsvUpdater callBack) {
        this.callBack = callBack;

    }

    private void updateImageIconList() {
        List<String> localFileLocationList = getFileNameList();
        for (String path : localFileLocationList) {
            imageIconList.add(getImageIcon(path));
        }
    }


    private void initAllTabImageIconAndName() {
        imageIconList = new ArrayList<>();
        List<String> list = getFileNameList();
        imageNames = new String[list.size()];
        int count = 0;
        for (String fileNameAndType : getFileNameList()) {
            imageNames[count++] = fileNameAndType.substring(fileNameAndType.lastIndexOf(File.separator) + 1);
        }
    }

    protected abstract List<String> getFileNameList();

    protected abstract List<Pair<String, String>> getFileNameAndTypeList();

    private ImageIcon getImageIcon(String path) {
        ImageIcon imageIcon = new ImageIcon(path);
        Image image = imageIcon.getImage();
        Image newImg = image.getScaledInstance(200, 160, Image.SCALE_DEFAULT);
        imageIcon = new ImageIcon(newImg);
        return imageIcon;
    }


    private class AddToVCSListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            callBack.addToCsv(imageList.getSelectedValuesList(), getFileNameAndTypeList());
        }
    }

}
