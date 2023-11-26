package xyz.whisperchat.client;

import java.util.List;

public class Application {
    public static void main(String[] args) throws Exception {
        ChatPluginLoader loader = new ChatPluginLoader();
        List<StylometricAnonymizer> extensions = loader.loadChatPlugin("C:\\Users\\elmcd\\Downloads\\llamacpu-plugin-0.1-all.jar");
        for (StylometricAnonymizer ext : extensions) {
            System.out.println(ext.anonymize("Test message"));
        }
    }
}
