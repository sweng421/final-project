package xyz.whisperchat.client.connection.messages.server;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ConfirmationStage {
    @JsonProperty("LOGIN")
    LOGIN,
    @JsonProperty("USERNAME")
    USERNAME
}
