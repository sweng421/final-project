package xyz.whisperchat.client.connection.messages;

import com.fasterxml.jackson.annotation.*;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.EXISTING_PROPERTY, 
    property = "kind", visible = true)
public abstract class TaggedJson {
    private String kind;
    public String getKind() {
        return kind;
    }
    public void setKind(String k) {
        kind = k;
    }
}
