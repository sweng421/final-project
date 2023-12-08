package xyz.whisperchat.client.connection;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.*;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import xyz.whisperchat.client.connection.messages.client.*;
import xyz.whisperchat.client.connection.messages.server.*;

public class ChatroomConnection extends WebSocketClient {
    private final ObjectReader jsonReader;
    private final ObjectWriter jsonWriter;
    private Set<MessageListener> listeners;
    private final ChatroomSettings settings;

    private ConnectionListener loginEvent = null;
    private String username = null;
    private char[] password = null;

    public ChatroomConnection(URI wsUri, ChatroomSettings s) {
        super(wsUri);
        listeners = new HashSet<>();
        settings = s;
        ObjectMapper mapper = new ObjectMapper();
        jsonReader = mapper.readerFor(ServerMessage.class);
        jsonWriter = mapper.writerFor(ClientRequest.class);
    }

    public void addListener(MessageListener l) {
        listeners.add(l);
    }

    public void removeListener(MessageListener l) {
        listeners.remove(l);
    }

    private void broadcastError(Exception e) {
        for (MessageListener l : listeners) {
            l.onError(e);
        }
        if (loginEvent != null) {
            loginEvent.onError(e);
        }
    }

    private void processConfirmation(ConfirmationMessage c) {
        switch (c.getMessage()) {
            case LOGIN:
                sendRequest(new UsernameRequest(username));
                break;
            case USERNAME:
                if (loginEvent != null) {
                    loginEvent.onConnect(this);
                    loginEvent = null;
                }
                break;
        }
    }

    @Override
    public void onOpen(ServerHandshake _handshake) {
        try {
            if (password != null) {
                try (ByteArrayOutputStream o = new ByteArrayOutputStream()) {
                    LoginRequest r = new LoginRequest(password);
                    jsonWriter.writeValue(o, r);
                    byte[] bytes = o.toByteArray();
                    r.clearPassword();
                    send(bytes);
                    for (int i = 0; i < bytes.length; i++) {
                        bytes[i] = 0;
                    }
                }
            } else {
                sendRequest(new UsernameRequest(username));
            }
        } catch (Exception e) {
            if (loginEvent != null) {
                loginEvent.onError(e);
            }
        }   
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {}

    @Override
    public void onMessage(String message) {
        try {
            ServerMessage m = jsonReader.readValue(message, ServerMessage.class);
            if (m instanceof PostMessage) {
                PostMessage p = (PostMessage)m;
                for (MessageListener l : listeners) {
                    l.onMessage(p);
                }
            } else if (m instanceof ErrorMessage) {
                ErrorMessage p = (ErrorMessage)m;
                broadcastError(new ServerException(p.getError()));
            } else if (m instanceof ConfirmationMessage) {
                processConfirmation((ConfirmationMessage)m);
            }
        } catch (Exception e) {
            broadcastError(e);
        }
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        broadcastError(ex);
    }

    private void sendRequest(ClientRequest r) {
        try {
            String json = jsonWriter.writeValueAsString(r);
            send(json);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void sendMessage(String msg) {
        sendRequest(new PostRequest(msg));
    }

    void identity(String usr, ConnectionListener l) {
        username = usr;
        loginEvent = l;
        connect();
    }

    void login(String usr, char[] pwd, ConnectionListener l) throws InterruptedException {
        password = pwd;
        identity(usr, l);
    }

    public ChatroomSettings getSettings() {
        return settings;
    }
}
