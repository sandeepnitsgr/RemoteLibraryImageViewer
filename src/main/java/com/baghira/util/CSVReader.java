package com.baghira.util;

import com.intellij.openapi.util.Pair;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class CSVReader {


    public static final String SINGLE_DPI = "singleDpi";
    public static final String MULTI_DPI = "multiDpi";
    public static final String WEBP = "webp";
    public static final String XXHDPI = "xxhdpi";
    public static final String CSV_SEPARATOR = ",";
    public static final String CUSTOMERAPP = "customerapp";
    public static final String CUSTOMERAPP_MID_APP = "customerapp_mid_app";
    public static final String CUSTOMERAPP_PRO = "customerapp_pro";
    public static final String SELLERAPP = "sellerapp";
    private final Set<Pair<String, String>> fileNameSet;
    private final String csvPath;

    public CSVReader(String basePath, String relativePath) {
        csvPath = basePath + "%s" + relativePath;
        fileNameSet = new TreeSet<>(Comparator.comparing(o -> o.first));
    }

    public void initReader() {
        initPathAndUpdateResultForCustomerapp();
        initPathAndUpdateResultForCustomerappPro();
        initPathAndUpdateResultForCustomerappMidApp();
        initPathAndUpdateResultForSellerapp();
        //initAllRemoteFiesAndUpdateResult();
    }

    private void initPathAndUpdateResultForCustomerappMidApp() {
        updateResultFromFileContent(initializeAndGetPath(CUSTOMERAPP_MID_APP));
    }

    private void initPathAndUpdateResultForCustomerappPro() {
        updateResultFromFileContent(initializeAndGetPath(CUSTOMERAPP_PRO));
    }

//    private void initAllRemoteFiesAndUpdateResult() {
//        String[] remoteFiles = AllRemoteFilesAndType.ALL_FILES.split("\n");
//        for (String remoteFileDetails : remoteFiles) {
//            fetchImageDetailsAndUpdateSet(remoteFileDetails);
//        }
//    }

    private void initPathAndUpdateResultForCustomerapp() {
        updateResultFromFileContent(initializeAndGetPath(CUSTOMERAPP));
    }

    private void initPathAndUpdateResultForSellerapp() {
        updateResultFromFileContent(initializeAndGetPath(SELLERAPP));
    }

    private void updateResultFromFileContent(String path) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                fetchImageDetailsAndUpdateSet(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchImageDetailsAndUpdateSet(String line) {
        String[] imagesInfo = line.split(CSV_SEPARATOR);
        String imgName = imagesInfo[0].trim();
        String fileType = imagesInfo[1].trim();
        if (isImage(fileType) && isNotWebpImage(imgName)) {
            String dpiType = XXHDPI;
            if (isSingleDensityImage(fileType)) {
                dpiType = SINGLE_DPI;
            }
            addImageToSet(imgName, dpiType);
        }
    }

    private void addImageToSet(String imgName, String dpiType) {
        fileNameSet.add(new Pair<>(imgName, dpiType));
    }


    private boolean isImage(String type) {
        return isSingleDensityImage(type) || isMultiDensityImage(type);
    }

    private boolean isSingleDensityImage(String type) {
        return type.equals(SINGLE_DPI);
    }

    private boolean isMultiDensityImage(String type) {
        return type.equals(MULTI_DPI);
    }

    private boolean isNotWebpImage(String imgName) {
        return !imgName.contains(WEBP);
    }

    private String initializeAndGetPath(String app) {
        return String.format(csvPath, app);
    }

    public List<Pair<String, String>> getDistinctFilesName() {
        return new ArrayList<>(fileNameSet);
    }
}
