package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ServerCommunicationThread extends Thread {
    private String username = "";
    private Socket socket;
    private Map<String, ServerCommunicationThread> userList;
    private BufferedReader input;
    private PrintWriter output;

    public ServerCommunicationThread(Socket socket, Map<String, ServerCommunicationThread> userList) throws IOException {
        this.socket = socket;
        this.userList = userList;

        input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        output = new PrintWriter(this.socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            while(true) {
                String outStream = input.readLine();

                if (outStream != null && (outStream.toLowerCase().equals("exit") || outStream.toLowerCase().equals("quit"))) {
                    userList.get(username).output.println("Server: Bye " + username + "!");
                    userList.remove(username);
                    break;
                }
//
                if (username.equals("")) {
                    username = outStream;
                    System.out.println(username + " registered.");
                    userList.put(username, this);
                    continue;
                }
//
                if (outStream.startsWith("/")) {
                    handleUserCommand(outStream.substring(1));
                    continue;
                }

                String message = username + ": " + outStream;

                System.out.println(message);
                printMessageToClients(message);
            }
        } catch (IOException e) {
            System.out.println("I/O exception: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void printMessageToClients(String message) {
        System.out.println("Sending to clients");
        for (ServerCommunicationThread client : userList.values()) {
            client.output.println(message);
        }
    }

    private void handleUserCommand(String command) {
        System.out.println(command);
    }
}
