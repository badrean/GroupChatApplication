package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private int port;
    private Map<String, ServerCommunicationThread> usersMap;

    public Server(int port) {
        this.port = port;
        this.usersMap = new HashMap<>();
    }

    public void runServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is running on port " + port);
            while(true) {
                Socket socket = serverSocket.accept();
                ServerCommunicationThread communicationThread = new ServerCommunicationThread(socket, usersMap);
                communicationThread.start();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
