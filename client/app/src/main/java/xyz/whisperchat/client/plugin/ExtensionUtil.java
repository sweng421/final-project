package xyz.whisperchat.client.plugin;

import java.io.File;

public interface ExtensionUtil {
    File fileDialog(String fileDescription, String extension);
    int getCharLimit();
}
