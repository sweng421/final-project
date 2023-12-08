package xyz.whisperchat.client.connection.messages.client;

import com.fasterxml.jackson.annotation.*;
import xyz.whisperchat.client.connection.messages.TaggedJson;

@JsonSubTypes({
    @JsonSubTypes.Type(value = LoginRequest.class, name = LoginRequest.KIND_KEY),
    @JsonSubTypes.Type(value = UsernameRequest.class, name = UsernameRequest.KIND_KEY),
    @JsonSubTypes.Type(value = PostRequest.class, name = PostRequest.KIND_KEY)
})
public abstract class ClientRequest extends TaggedJson {}
