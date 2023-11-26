package xyz.whisperchat.client;

import org.pf4j.DefaultPluginManager;
import java.nio.file.Path;
import java.util.List;

/*public class MyPluginManager extends DefaultPluginManager  {

    // code
    
    public void loadAndStartGreetingService( String plugin ) {
        Path path = getPluginsRoot().resolve(plugin); //  plugin = "hello-greeting-0.0.1-SNAPSHOT.zip"
       
        String pluginId = this.loadPlugin(path);

        this.startPlugin(pluginId);

        List<TsdbService> extensions = this.getExtensions(Greeting.class, pluginId); // Greeting is the extension endpoint
        
	// code
    }

}*/
public class ChatPluginLoader extends DefaultPluginManager {
    public List<StylometricAnonymizer> loadChatPlugin(String plugin) {
        System.out.println(getPluginsRoot());
        Path path = getPluginsRoot().resolve(plugin);
        System.out.println(path);
        
        String pluginId = this.loadPlugin(path);
        this.startPlugin(pluginId);

        return this.getExtensions(StylometricAnonymizer.class, pluginId);
    }

}
