package com.pvz2.network;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private static final Gson GSON = NetworkGson.INSTANCE; // compact — one JSON object per line, no embedded newlines

    private final Socket socket;
    private final GameServer server;
    private BufferedReader in;
    private PrintWriter out;
    private volatile String username; // null until this connection successfully logs in

    public ClientHandler(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            String line;
            while ((line = in.readLine()) != null) {
                try {
                    NetworkMessage msg = GSON.fromJson(line, NetworkMessage.class);
                    if (msg == null || msg.type == null) continue;
                    server.dispatch(this, msg);
                } catch (Exception e) {
                    System.err.println("Malformed message from " +
                        (username != null ? username : socket.getRemoteSocketAddress()) + ": " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Connection lost: " +
                (username != null ? username : socket.getRemoteSocketAddress()));
        } finally {
            disconnect();
        }
    }

    /** Sends one message to this specific client. Safe to call from any thread. */
    public void send(String type, String requestId, Object payload) {
        NetworkMessage msg = new NetworkMessage(type, GSON.toJsonTree(payload));
        msg.requestId = requestId;
        String line = GSON.toJson(msg);
        synchronized (this) {
            out.println(line);
        }
    }

    private void disconnect() {
        server.onDisconnect(this);
        try { socket.close(); } catch (IOException ignored) {}
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
