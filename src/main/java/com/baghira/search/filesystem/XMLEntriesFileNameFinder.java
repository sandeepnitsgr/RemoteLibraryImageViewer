package com.baghira.search.filesystem;

import com.intellij.openapi.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class XMLEntriesFileNameFinder implements FileNameFinder<Set<Pair<String, String>>> {
    public Map<String, String> idAndDpiTypeMap;
    private static final String REMOTE_FILE_NAME_ATTRIBUTE = "remoteFileName=";
    private static final String DPI_TYPE_ATTRIBUTE = "imageDpiSupportType=";
    private static final String ID_ATTRIBUTE = ":id=\"@+id/";

    public XMLEntriesFileNameFinder() {
        idAndDpiTypeMap = new HashMap<>();
    }

    @Override
    public Set<Pair<String, String>> findAllFileNames(String basePath, String rawString) {
        HashSet<Pair<String, String>> result = new HashSet<>();
        String[] separateTags = rawString.split("--");

        for (String tag : separateTags) {
            if (!tag.contains("DeferredImageView")) continue;
            String[] individualTags = tag.split("/>");

            String concernedString = individualTags[0];
            int fileNameAttributeIndex = concernedString.indexOf(REMOTE_FILE_NAME_ATTRIBUTE);
            int dpiTypeAttributeIndex = concernedString.indexOf(DPI_TYPE_ATTRIBUTE);
            int idIndex = concernedString.indexOf(ID_ATTRIBUTE);
            String fileNameAttributeValue = null;
            String dpiTypeAttributeValue = null;
            String idValue = null;
            if (fileNameAttributeIndex != -1) {
                fileNameAttributeIndex += REMOTE_FILE_NAME_ATTRIBUTE.length() + 1;
                fileNameAttributeValue = concernedString.substring(fileNameAttributeIndex, fileNameAttributeIndex + concernedString.substring(fileNameAttributeIndex).indexOf("\""));
            }
            if (dpiTypeAttributeIndex != -1) {
                dpiTypeAttributeIndex += DPI_TYPE_ATTRIBUTE.length() + 1;
                dpiTypeAttributeValue = concernedString.substring(dpiTypeAttributeIndex, dpiTypeAttributeIndex + concernedString.substring(dpiTypeAttributeIndex).indexOf("\""));
            }
            String dpi = dpiTypeAttributeValue != null && dpiTypeAttributeValue.contains("single") ? "singleDpi" : "xxhdpi";
            if (idIndex != -1) {
                idIndex += ID_ATTRIBUTE.length();
                idValue = concernedString.substring(idIndex, idIndex + concernedString.substring(idIndex).indexOf("\""));
                idAndDpiTypeMap.put(idValue, dpi);
            }

            if (fileNameAttributeValue != null && !fileNameAttributeValue.contains("webp"))
                result.add(new Pair<>(fileNameAttributeValue, dpi));
        }

        return result;
    }

    public Map<String, String> getIdAndDpiTypeMap() {
        return idAndDpiTypeMap;
    }


}
