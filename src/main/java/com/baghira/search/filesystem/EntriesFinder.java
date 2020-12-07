package com.baghira.search.filesystem;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

public class EntriesFinder {

    private static final String BASE_COMMAND = "git grep ";
    public static final String METHOD_LOAD_REMOTE_IMAGE_DRAWABLE = ".loadRemoteImageDrawable(";
    public static final String TAG_DEFERRED_IMAGE_VIEW = "<com.tkpd.remoteresourcerequest.view.DeferredImageView";
    private String basePath;
    private HashMap<String, String> result;

    public EntriesFinder(String basePath) {
        this.basePath = basePath;
    }

    private String searchEntry(String directory, String aCommand) {
        String output = "";
        try {
            Runtime rt = Runtime.getRuntime();
            String executableCommand = BASE_COMMAND + aCommand;
            Process proc = rt.exec(executableCommand, null, new File(directory));
            StreamGobbler errorGobbler = new
                    StreamGobbler(proc.getErrorStream(), "ERROR");

            StreamGobbler outputGobbler = new
                    StreamGobbler(proc.getInputStream(), "OUTPUT");

            errorGobbler.start();
            outputGobbler.start();

            output = outputGobbler.getOutput();
            System.out.println("Final output: " + output);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return output;
    }

    public Object getAllRelevantEntries() {

        String methodSearchResult = searchEntry(basePath, METHOD_LOAD_REMOTE_IMAGE_DRAWABLE);
        String tagSearchResult = searchEntry(basePath, TAG_DEFERRED_IMAGE_VIEW);
        processAndAddToResult(methodSearchResult, tagSearchResult);
        return result;
    }

    private void processAndAddToResult(String... resultArgs) {
        for(String result : resultArgs) {
            addToResult(getProcessedResult(result));
        }
    }

    private void addToResult(Object processedResult) {

    }

    private String getProcessedResult(String result) {
        return result;
    }
}
