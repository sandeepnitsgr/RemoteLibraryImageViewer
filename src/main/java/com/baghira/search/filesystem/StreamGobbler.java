package com.baghira.search.filesystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamGobbler extends Thread {
    InputStream is;
    String type;
    StringBuffer output = new StringBuffer();

    StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line).append("\r\n");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String getOutput() {
        return this.output.toString();
    }
}