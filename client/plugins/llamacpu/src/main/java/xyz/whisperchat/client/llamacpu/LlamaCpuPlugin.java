package xyz.whisperchat.client.llamacpu;

import org.pf4j.Extension;
import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;
import xyz.whisperchat.client.StylometricAnonymizer;
import xyz.whisperchat.client.ExtensionUtil;

public class LlamaCpuPlugin extends Plugin {
    public LlamaCpuPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Override
    public void start() {
        System.out.println("Starting LLAMA CPU plugin");
    }

    @Override
    public void stop() {
        System.out.println("Stopping LLAMA CPU plugin");
    }

    @Extension(ordinal=1)
    public static class LlamaCpuExt implements StylometricAnonymizer {
        public String anonymize(String message) throws Exception {
            return "LLAMA: " + message;
        }

        public void setup(ExtensionUtil util) throws Exception {
        }
    }
}
