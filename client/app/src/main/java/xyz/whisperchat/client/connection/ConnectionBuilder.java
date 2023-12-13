package xyz.whisperchat.client.connection;

import java.net.URL;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

public class ConnectionBuilder {
    private ChatroomSettings settings = null;
    private String host = null, username = null;
    private Component parent = null;

    public ConnectionBuilder setServer(String serverHost) throws IOException, URISyntaxException {
        HttpURLConnection httpConn = null;
        URI endpointUri = new URI("https", serverHost, "/settings", null, null);
        URL settingsEndpoint = endpointUri.toURL();
    
        try {
            httpConn = (HttpURLConnection)settingsEndpoint.openConnection();
            httpConn.setRequestMethod("GET");
            try (InputStream payload = httpConn.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                settings = mapper.readValue(payload, ChatroomSettings.class);
            }
            host = serverHost;
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        return this;
    }

    public ConnectionBuilder setUsername(String usr) {
        username = usr;
        return this;
    }

    public ConnectionBuilder setPromptParent(Component p) {
        parent = p;
        return this;
    }

    private char[] promptPassword() {
        JPasswordField pwdInput = new JPasswordField();
        final JComponent[] dialogItems = new JComponent[] {
            new JLabel("Please enter chatroom password"),
            pwdInput,
        };
        int result = JOptionPane.showConfirmDialog(
            parent, dialogItems, "Password required",
            JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                return pwdInput.getPassword();
            } catch (NullPointerException e) {
                // fall through
            }
        }
        return new char[0];        
    }

    public CompletableFuture<ChatroomConnection> connect() throws URISyntaxException, InterruptedException {
        URI wsUri = new URI("wss", host,
            settings.getChatPath(), null, null);
        final ChatroomConnection connection = new ChatroomConnection(host, wsUri, settings);
        final CompletableFuture<ChatroomConnection> f = new CompletableFuture<>();

        ConnectionListener listener = new ConnectionListener() {
            @Override
            public void onConnect(ChatroomConnection conn) {
                f.complete(conn);
            }
            @Override
            public void onError(Exception e) {
                connection.close();
                f.completeExceptionally(e);
            }
        };

        if (settings.getRequiresPassword()) {
            connection.login(username, promptPassword(), listener);
        } else {
            connection.identity(username, listener);
        }
        return f;
    }
}
