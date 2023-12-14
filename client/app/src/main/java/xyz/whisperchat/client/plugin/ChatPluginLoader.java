/**
 * PATTERN: Dynamic Loading
 * 
 * Dynamic loading is used to load plugins from
 * JAR files. Classes that implement the StylometricAnonymizer
 * are extracted and used to anonymize the user input
*/

package xyz.whisperchat.client.plugin;

import org.pf4j.DefaultPluginManager;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class ChatPluginLoader extends DefaultPluginManager {
    public StylometricAnonymizer loadChatPlugin(File pluginFile) throws NoPluginFound {
        System.out.println(getPluginsRoot());
        Path path = getPluginsRoot().resolve(pluginFile.getAbsolutePath());
        System.out.println(path);
        
        String pluginId = this.loadPlugin(path);
        this.startPlugin(pluginId);

        List<StylometricAnonymizer> exts = getExtensions(StylometricAnonymizer.class, pluginId);
        if (exts.isEmpty()) {
            throw new NoPluginFound(pluginFile);
        }
        return exts.get(0);
    }
}
