package com.baghira.ui.tabs;


import com.baghira.downloader.CsvUpdater;
import com.baghira.ui.DownloadedImageCellRenderer;
import com.baghira.util.TextResources;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class CriteriaTabsAbstract extends JPanel {
    JBScrollPane scrollPane;
    JBList<String> imageList;
    JButton button;
    JLabel numOfImages;
    private List<ImageIcon> imageIconList;
    private String[] imageNames;
    private CsvUpdater callBack;


    public CriteriaTabsAbstract() {
        imageList = new JBList<>();
        imageList.setBorder(JBUI.Borders.empty(5));
        imageList.setFixedCellWidth(190);
        imageList.setFixedCellHeight(170);
        scrollPane = new JBScrollPane(imageList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button = new JButton("Add To CSV");
        button.setOpaque(true);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        setLayout(new BorderLayout());
        numOfImages = new JLabel();
        add(numOfImages, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(button, BorderLayout.SOUTH);
        button.addMouseListener(new AddToVCSListener());
    }

    protected abstract boolean shouldEnableSelection();

    protected abstract boolean getButtonVisibility();

    public void initData() {
        initAllTabImageIconAndName();
        updateImageIconList();
        imageList.setListData(imageNames);
        imageList.setCellRenderer(new DownloadedImageCellRenderer(imageIconList));
        String additionalHeaderText = getAdditionalHeaderText();
        String headerText = "<html>    " + imageNames.length + " Images. " +
                (additionalHeaderText != null ? "<font color=\"orange\">" + additionalHeaderText + "</font></html>" : "");

        numOfImages.setText(headerText);
        numOfImages.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        numOfImages.setVisible(true);
        if (shouldEnableSelection()) {
            imageList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            imageList.addListSelectionListener(listSelectionEvent -> {
                int[] selectedIndexes = imageList.getSelectedIndices();
                for (int index : selectedIndexes) {
                    imageList.getModel().getElementAt(index);
                }
            });
        } else {
            imageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        button.setVisible(getButtonVisibility());
    }

    protected abstract String getAdditionalHeaderText();

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
        for (String fileNameAndType : list) {
            imageNames[count++] = fileNameAndType.substring(fileNameAndType.lastIndexOf(File.separator) + 1);
        }
    }

    protected abstract List<String> getFileNameList();

    private ImageIcon getImageIcon(String path) {
        ImageIcon imageIcon = new ImageIcon(path);
        Image image = imageIcon.getImage();
        Image newImg = image.getScaledInstance(200, 160, Image.SCALE_DEFAULT);
        imageIcon = new ImageIcon(newImg);
        return imageIcon;
    }


    private class AddToVCSListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            List<String> selectedList = imageList.getSelectedValuesList();
            if (selectedList.size() > 0) {
                int val = shouldAddToCustomerapp();
                if (val >= 0 && val != 2)
                    callBack.addToCsv(val == 0, selectedList);
            } else
                callBack.showNotification("<b>Note</b><br/><b>Please select at least 1 image from non-prefetched list!</b>");
        }

        private int shouldAddToCustomerapp() {
            Object[] options = new Object[]{TextResources.getCustomerapp(), TextResources.getSellerapp(), TextResources.getCancel()};
            return JOptionPane.showOptionDialog(scrollPane,
                    TextResources.getShouldAddToCustomerappString(),
                    TextResources.getCustomerSellerappTitle(),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        }

        @Override
        public void mousePressed(MouseEvent e) {
            button.setForeground(JBColor.GREEN);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            button.setForeground(UIManager.getColor("java.lang.String"));
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            button.setOpaque(true);
            button.setBackground(JBColor.GREEN);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            button.setBackground(UIManager.getColor("control"));

        }
    }

}
