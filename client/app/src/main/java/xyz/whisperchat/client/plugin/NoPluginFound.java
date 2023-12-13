package xyz.whisperchat.client.plugin;

import java.io.File;

public class NoPluginFound extends Exception {
    private File file;
    public NoPluginFound(File f) {
        super("No plugin found in file " + f.getName());
        f = file;
    }
    public File getFile() {
        return file;
    }
}
