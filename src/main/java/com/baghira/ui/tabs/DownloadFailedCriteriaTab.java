package com.baghira.ui.tabs;

import com.baghira.downloader.CsvUpdater;

import java.util.List;

public class DownloadFailedCriteriaTab extends CriteriaTabsAbstract {
    private List<String> fileNamesList;

    public DownloadFailedCriteriaTab(List<String> fileNamesList) {
        this.fileNamesList = fileNamesList;
        initData();
    }

    @Override
    protected boolean shouldEnableSelection() {
        return false;
    }

    @Override
    protected boolean getButtonVisibility() {
        return false;
    }

    @Override
    protected String getAdditionalHeaderText() {
        return fileNamesList.size() > 0 ?
                "<br/>" +
                        "<b>" +
                        "Following files failed to download. Possible failure cause:" +
                        "<br>" +
                        "<ul>" +
                        "<li>Misspelled file name</li>" +
                        "<li>Wrong file extension</li>" +
                        "<li>Wrong density type mentioned in supportDpiType</li>" +
                        "<li>File not uploaded on server</li>" +
                        "</ul> " : null;
    }

    @Override
    protected List<String> getFileNameList() {
        return fileNamesList;
    }
}
