package xyz.whisperchat.client;

import xyz.whisperchat.client.plugin.ChatPluginLoader;
import xyz.whisperchat.client.plugin.ExtensionUtil;
import xyz.whisperchat.client.plugin.StylometricAnonymizer;
import xyz.whisperchat.client.plugin.UtilImpl;

public class Application {
    public static void main(String[] args) throws Exception {
        ChatPluginLoader loader = new ChatPluginLoader();
        ExtensionUtil util = new UtilImpl(500);
        String jarpath = "C:\\Users\\beaco\\Documents\\final-project\\client\\plugins\\llamafier\\target\\llama-anon-plugin-0.1.jar";
        try (StylometricAnonymizer ext = loader.loadChatPlugin(jarpath)) {
            ext.setup(util);
            System.out.println(ext.anonymize("This is a test message"));
        }
    }
}
