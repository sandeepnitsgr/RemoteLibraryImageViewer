package com.baghira.util;

import com.intellij.openapi.util.Pair;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CSVReaderAndWriter {


    public static final String SINGLE_DPI = "singleDpi";
    public static final String MULTI_DPI = "multiDpi";
    public static final String WEBP = "webp";
    public static final String XXHDPI = "xxhdpi";
    public static final String CSV_SEPARATOR = ",";
    public static final String CUSTOMERAPP_MID_APP = "customer_mid_app";
    public static final String SELLER_APP = "sellerapp";
    private final Set<Pair<String, String>> fileNameSet;
    private final String csvPath;
    private List<String> csvModuleNameList;

    public CSVReaderAndWriter(String basePath, String relativePath) {
        csvPath = basePath + "%s" + relativePath;
        fileNameSet = new TreeSet<>(Comparator.comparing(o -> o.first));
    }

    public void initReader(List<String> csvPathList) {
        csvModuleNameList = csvPathList;
        initPathAndUpdateResult(csvPathList);
    }

    private void initPathAndUpdateResult(List<String> csvPathList) {
        for (String moduleName : csvPathList)
            updateResultFromFileContent(initializeAndGetPath(moduleName));
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

    public Set<Pair<String, String>> getDistinctFilesName() {
        return fileNameSet;
    }

    public void writeToCSV(boolean shouldAddToCustomerapp, List<Pair<String, String>> fileNameAndTypeList) {

        String path;
        try {
            path = initializeAndGetPath(csvModuleNameList.get(shouldAddToCustomerapp ? 0 : 1));
        } catch (Exception e) {
            try {
                path = initializeAndGetPath(CUSTOMERAPP_MID_APP);
            } catch (Exception ignore) {
                path = initializeAndGetPath(SELLER_APP);
            }
        }
        try {
            FileWriter csvWriter = new FileWriter(path, true);

            for (Pair<String, String> nameAndType : fileNameAndTypeList) {
                csvWriter.append("\n");
                csvWriter.append(nameAndType.first);
                csvWriter.append(" ");
                csvWriter.append(",");
                if (nameAndType.second.equals(XXHDPI)) {
                    csvWriter.append(MULTI_DPI);
                } else {
                    csvWriter.append(SINGLE_DPI);
                }
                csvWriter.append(",");
                csvWriter.append("   ");
                csvWriter.append(",");
            }

            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
