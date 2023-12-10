package xyz.whisperchat.client.plugin;

import org.pf4j.ExtensionPoint;

public interface StylometricAnonymizer extends ExtensionPoint, AutoCloseable {
    String anonymize(String message) throws Exception;
    void setup(ExtensionUtil util) throws Exception;
}
