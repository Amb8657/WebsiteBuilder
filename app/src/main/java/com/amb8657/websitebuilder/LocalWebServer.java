package com.amb8657.websitebuilder;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

/** Small offline phone-host for the MVP. It serves the current static site over the device LAN. */
public class LocalWebServer {
    private ServerSocket serverSocket;
    private Thread thread;
    private volatile boolean running;
    private final String html;
    private int port;

    public LocalWebServer(String html) { this.html = html == null ? "" : html; }

    public synchronized void start() throws IOException {
        if (running) return;
        serverSocket = new ServerSocket(0);
        port = serverSocket.getLocalPort();
        running = true;
        thread = new Thread(() -> {
            while (running) {
                try {
                    Socket socket = serverSocket.accept();
                    handle(socket);
                } catch (IOException ignored) {
                    if (!running) break;
                }
            }
        }, "website-builder-server");
        thread.start();
    }

    private void handle(Socket socket) {
        try (Socket s = socket; BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8)); OutputStream out = s.getOutputStream()) {
            String line = in.readLine();
            while (line != null && !line.isEmpty()) line = in.readLine();
            byte[] body = html.getBytes(StandardCharsets.UTF_8);
            String header = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\nContent-Length: " + body.length + "\r\nConnection: close\r\n\r\n";
            out.write(header.getBytes(StandardCharsets.UTF_8));
            out.write(body);
            out.flush();
        } catch (IOException ignored) { }
    }

    public synchronized void stop() {
        running = false;
        if (serverSocket != null) {
            try { serverSocket.close(); } catch (IOException ignored) { }
        }
        serverSocket = null;
        thread = null;
    }

    public boolean isRunning() { return running; }
    public int getPort() { return port; }
}
