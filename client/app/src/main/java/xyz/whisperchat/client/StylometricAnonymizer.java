package xyz.whisperchat.client;

import org.pf4j.ExtensionPoint;

public interface StylometricAnonymizer extends ExtensionPoint {
    String anonymize(String message) throws Exception;
    void setup(ExtensionUtil util) throws Exception;
}
