package xyz.whisperchat.client.plugin;

import org.pf4j.DefaultPluginManager;

import java.nio.file.Path;
import java.util.List;

public class ChatPluginLoader extends DefaultPluginManager {
    public StylometricAnonymizer loadChatPlugin(String plugin) {
        System.out.println(getPluginsRoot());
        Path path = getPluginsRoot().resolve(plugin);
        System.out.println(path);
        
        String pluginId = this.loadPlugin(path);
        this.startPlugin(pluginId);

        List<StylometricAnonymizer> exts = getExtensions(StylometricAnonymizer.class, pluginId);
        return exts.isEmpty() ? null : exts.get(0);
    }
}
