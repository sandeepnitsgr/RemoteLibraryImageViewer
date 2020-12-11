package com.baghira.search.filesystem;


import com.intellij.openapi.util.Pair;

import java.io.File;
import java.util.HashSet;

public class JavaEntriesFileNameFinder implements FileNameFinder {
    @Override
    public HashSet<Pair<String, String>> findAllFileNames(String basePath, String rawString) {
        HashSet<Pair<String, String>> fileNames = new HashSet<>();
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
            String str = "loadRemoteImageDrawable(";
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
                }
                fileNames.add(new Pair<>(fileName, type));
            } else {
                String path = fileAndUsage[0].trim();
                String res = EntriesFinder.searchEntry(basePath + File.separator + path.substring(0, path.lastIndexOf(File.separator)), var);
                String[] rr = res.split("\\n");
                if (rr.length == 1) {
                    String[] localRR = res.split(":");
                    String valStr = localRR[1].trim();

                    if (valStr.contains(".png")) {
                        String name = valStr.substring(valStr.indexOf("\"") + 1, valStr.lastIndexOf("\""));

                        if (valStr.contains("ImageDensityType")) {
                            String[] densityImage = valStr.split(",");
                            String typeSub = densityImage[1].trim().toLowerCase();
                            if (typeSub.contains("single")) {
                                type = "singleDpi";
                            } else {
                                type = "xxhdpi";
                            }
                        }
                        fileNames.add(new Pair<>(name, type));
                    }
                } else {
                    for (String sst : rr) {
                        if (sst == null || sst.isEmpty() || !sst.contains(":"))
                            continue;
                        String[] colSplit = sst.split(":");
                        String us = colSplit[1].trim();
                        if (us.contains(".png")) {
                            String name = us.substring(us.indexOf("\"") + 1, us.lastIndexOf("\""));
                            if (us.contains("ImageDensityType")) {
                                String[] densityImage = us.split(",");
                                String typeSub = densityImage[1].trim().toLowerCase();
                                if (typeSub.contains("single")) {
                                    type = "singleDpi";
                                } else {
                                    type = "xxhdpi";
                                }
                            }
                            fileNames.add(new Pair<>(name, type));
                            break;
                        }
                    }
                }
            }
        }
        return fileNames;
    }
}
