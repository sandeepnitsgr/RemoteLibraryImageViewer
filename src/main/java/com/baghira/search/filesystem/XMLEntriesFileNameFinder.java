package com.baghira.search.filesystem;

import com.intellij.lang.xml.XMLLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;

import java.io.File;
import java.util.*;

public class XMLEntriesFileNameFinder implements FileNameFinder<Set<Pair<String, String>>> {
    Map<String, String> dpiTypeAndFileName;
    private final Project project;
    private final HashSet<String> pathSet;

    public XMLEntriesFileNameFinder(Project project) {
        this.project = project;
        dpiTypeAndFileName = new HashMap<>();
        pathSet = new HashSet<>();
    }

    @Override
    public Set<Pair<String, String>> findAllFileNames(String basePath, String rawString) {
        TreeSet<Pair<String, String>> result = new TreeSet<>(Comparator.comparing(o -> o.first));
        String[] entries = rawString.split("\\n");
        for (String entry : entries) {
            if (entry == null || entry.isEmpty() || !entry.contains("=") || entry.contains("webp")) continue;
            String[] nameParts = entry.split(".png");
            String[] parts = entry.split(": ");
            String path = parts[0].trim();
            if (!pathSet.contains(path)) {
                pathSet.add(path);
                System.out.println("path to search = "+("file://" + project.getBasePath() + File.separator + path));
                VirtualFile virtualFile = VirtualFileManager.getInstance().findFileByUrl("file://" + project.getBasePath() + File.separator + path);
                PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
                XmlFile xmlFile;
                if (file != null) {
                    FileViewProvider viewProvider = file.getViewProvider();
                    xmlFile = (XmlFile) viewProvider.getPsi(XMLLanguage.INSTANCE);
                    if (xmlFile != null) {
                        searchAndAddDpiResult(xmlFile.getRootTag());
                    }
                }
            }

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

    private void searchAndAddDpiResult(XmlTag rootTag) {
        XmlTag[] tags = rootTag.getSubTags();
        for (XmlTag tag : tags) {
            if (tag != null) {
                if (tag.getName().equals("com.tkpd.remoteresourcerequest.view.DeferredImageView")) {
                    XmlAttribute[] attributes = tag.getAttributes();
                    String type = "xxhdpi";
                    String name = null;
                    for (XmlAttribute attribute : attributes) {
                        if (attribute.getName().contains("remoteFileName")) {
                            name = attribute.getValue();
                        } else if (attribute.getName().contains("imageDpiSupportType")) {
                            if (attribute.getValue().toLowerCase().contains("single")) {
                                type = "singleDpi";
                            }
                        }
//                        System.out.println(attribute.getName() + " : " + attribute.getValue());
                    }

                    if (name.contains("topads")) {
                        System.out.println("name = " + name + " : type = " + type);
                    }
                    dpiTypeAndFileName.put(name, type);
                    System.out.println();
                } else {
                    searchAndAddDpiResult(tag);
                }
            }
        }
    }
}
