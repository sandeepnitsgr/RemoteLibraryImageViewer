package com.baghira.search.filesystem;

import com.intellij.openapi.util.Pair;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class XMLEntriesFileNameFinder implements FileNameFinder {
    String typeResult;
    Map<String, String> dpiTypeAndFileName;

    public XMLEntriesFileNameFinder(String densityTypeResult) {
        this.typeResult = densityTypeResult;
        dpiTypeAndFileName = new HashMap<>();
    }

    @Override
    public HashSet<Pair<String, String>> findAllFileNames(String basePath, String rawString) {
        HashSet<Pair<String, String>> result = new HashSet<>();
        findFileNameForDensityType();
        String[] entries = rawString.split("\\n");
        for (String entry : entries) {
            if (entry == null || entry.isEmpty() || !entry.contains("=") || entry.contains("webp")) continue;
            String[] nameParts = entry.split(".png");
            String[] parts = entry.split(": ");
            String fileName = parts[0].substring(parts[0].lastIndexOf(File.separator) + 1);
            String firstPart = nameParts[0];
            String name = firstPart.substring(firstPart.lastIndexOf("\"") + 1) + ".png";
            String fileType = "xxhdpi";
            if (dpiTypeAndFileName.containsKey(fileName)) {
                fileType = dpiTypeAndFileName.get(fileName);
            }
            result.add(new Pair<>(name, fileType));
        }

        return result;
    }

    private void findFileNameForDensityType() {
        if (typeResult == null || typeResult.isEmpty() || !typeResult.contains(": ")) return;
        String[] type = typeResult.split(": ");
        String fileName = type[0].substring(type[0].lastIndexOf(File.separator) + 1);
        String dpiType = type[1].contains("single") ? "singleDpi" : "xxhdpi";
        dpiTypeAndFileName.put(fileName, dpiType);
    }
}
