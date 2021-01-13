package com.baghira.search.filesystem;

import java.io.File;

public class EntriesFinder {

    private static final String BASE_COMMAND = "git grep ";
    public static final String ERROR = "ERROR";
    public static final String OUTPUT = "OUTPUT";

    public static String searchEntry(String directory, String aCommand) {
        String output = "";
        try {
            Runtime rt = Runtime.getRuntime();
            String executableCommand = BASE_COMMAND + aCommand;
            Process proc = rt.exec(executableCommand, null, new File(directory));
            StreamGobbler errorGobbler = new
                    StreamGobbler(proc.getErrorStream(), ERROR);

            StreamGobbler outputGobbler = new
                    StreamGobbler(proc.getInputStream(), OUTPUT);

            errorGobbler.start();
            outputGobbler.start();
            outputGobbler.join();
            errorGobbler.join();
            output = outputGobbler.getOutput();

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return output;
    }

}
