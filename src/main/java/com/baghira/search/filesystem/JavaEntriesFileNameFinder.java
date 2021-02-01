package com.baghira.search.filesystem;

import com.intellij.openapi.util.Pair;

import java.io.File;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class JavaEntriesFileNameFinder implements FileNameFinder<Set<Pair<String, String>>> {
    Map<String, String> idAndDpiTypeMap;

    public JavaEntriesFileNameFinder(Map<String, String> idAndDpiTypeMap) {
        this.idAndDpiTypeMap = idAndDpiTypeMap;
    }

    @Override
    public Set<Pair<String, String>> findAllFileNames(String basePath, String rawString) {
        TreeSet<Pair<String, String>> fileNames = new TreeSet<>(Comparator.comparing(o -> o.first));
        String[] ss = rawString.split("\\n");
        for (String s : ss) {
            if (s == null ||
                    s.isEmpty() ||
                    !s.contains(":") ||
                    s.contains("DeferredImageView.kt") ||
                    s.contains("Test") ||
                    s.contains("abc") ||
                    s.contains("other_image") ||
                    s.contains("webp"))
                continue;
            String type = "xxhdpi";
            String[] fileAndUsage = s.split(":");
            String usage = fileAndUsage[1].trim();
            String str = ".loadRemoteImageDrawable(";
            int index = usage.indexOf(str);
            if (index == -1) continue;
            String var = usage.substring(index + str.length());
            var = var.substring(0, var.indexOf(")"));

            if (var.contains(".png")) {
                String fileName;
                if (var.contains("ImageDensityType")) {
                    String[] densityImage = var.split(",");
                    fileName = densityImage[0].substring(1, densityImage[0].length() - 1);
                    String typeSub = densityImage[1].trim().toLowerCase();
                    if (typeSub.contains("single")) {
                        type = "singleDpi";
                    } else {
                        type = "xxhdpi";
                    }
                } else {
                    fileName = var.substring(1, var.length() - 1);
                    String existingDpi = getExistingUsageFromXml(usage, index);
                    System.out.println("****************** " + existingDpi);
                    if (!existingDpi.isEmpty() && existingDpi.endsWith("?")) {
                        existingDpi = existingDpi.substring(0, existingDpi.length() - 1);
                    }
                    if (idAndDpiTypeMap.containsKey(existingDpi)) {
                        type = idAndDpiTypeMap.get(existingDpi);
                    }
                }
                fileNames.add(new Pair<>(fileName, type));
            } else {
                String path = fileAndUsage[0].trim();
                String searchString = var;
                if (searchString.contains("ImageDensityType")) {
                    if (searchString.toLowerCase().contains("single")) {
                        type = "singleDpi";
                    }
                    searchString = searchString.substring(0, searchString.indexOf(","));
                }
                String res = EntriesFinder.searchEntry(basePath + File.separator + path.substring(0, path.lastIndexOf(File.separator)), searchString);
                String[] rr = res.split("\\n");

                String existingDpi = getExistingUsageFromXml(usage, index);
                System.out.println("****************** " + existingDpi);
                if (!existingDpi.isEmpty() && existingDpi.endsWith("?")) {
                    existingDpi = existingDpi.substring(0, existingDpi.length() - 1);
                }
                if (idAndDpiTypeMap.containsKey(existingDpi)) {
                    type = idAndDpiTypeMap.get(existingDpi);
                }
                for (String sst : rr) {
                    if (sst == null || sst.isEmpty() || !sst.contains("png"))
                        continue;
                    String[] localRR = sst.split(":");
                    String filePath = path.substring(path.lastIndexOf(File.separator) + 1);
                    String currPath = localRR[0].trim();
                    currPath = currPath.substring(currPath.lastIndexOf(File.separator) + 1);
                    if (filePath.equals(currPath)) {
                        String name = localRR[1].trim();
                        name = name.substring(name.indexOf("\"") + 1, name.lastIndexOf("\""));
                        fileNames.add(new Pair<>(name, type));
                        break;
                    }
                }
            }
        }
        return fileNames;
    }

    private String getExistingUsageFromXml(String usage, int index) {
        char[] str = usage.toCharArray();
        int i = index - 1;
        StringBuilder sb = new StringBuilder();
        while (i >= 0 && (str[i] == '_' || str[i] == '?' || ((str[i] >= 'a' && str[i] <= 'z') || (str[i] >= 'A' && str[i] <= 'Z')))) {
            sb.append(str[i]);
            i--;
        }
        return sb.reverse().toString();
    }
}
