package xyz.whisperchat.client.connection.messages.server;

import com.fasterxml.jackson.annotation.*;
import xyz.whisperchat.client.connection.messages.TaggedJson;

@JsonSubTypes({
    @JsonSubTypes.Type(value = PostMessage.class, name = "message"),
    @JsonSubTypes.Type(value = ErrorMessage.class, name = "error"),
    @JsonSubTypes.Type(value = ConfirmationMessage.class,
        name = "confirmation")
})
public abstract class ServerMessage extends TaggedJson {}
