package com.baghira.ui.tabs;

import com.intellij.openapi.util.Pair;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;

public class NonPreFetchedCriteriaTab extends CriteriaTabsAbstract {

    private final List<String> fileNamesList;
    private final List<Pair<String, String>> nonPreFetchedFileNameAndTypeList;

    public NonPreFetchedCriteriaTab(List<Pair<String, String>> nonPreFetchedFileNameAndTypeList, List<String> fileNamesList) {
        //super();
        this.fileNamesList = fileNamesList;
        this.nonPreFetchedFileNameAndTypeList = nonPreFetchedFileNameAndTypeList;
        initData();
        initUI();
    }

    private void initUI() {
        GridLayout layout = new GridLayout(0, 2);
        imageList = new JBList<>();
        JBList<JButton> jButtonJList = new JBList<>();
        scrollPane = new JBScrollPane(imageList);

        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

    }

    @Override
    protected List<String> getFileLocationOfImages() {
        return fileNamesList;
    }

    @Override
    protected List<Pair<String, String>> getFileNameAndTypeList() {
        return nonPreFetchedFileNameAndTypeList;
    }
}
