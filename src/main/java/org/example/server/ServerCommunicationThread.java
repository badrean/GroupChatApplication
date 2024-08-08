package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

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
                    this.output.println("Server: Bye " + username + "!");
                    this.userList.remove(username);
                    break;
                }
//
                if (username.equals("")) {
                    if (isUsernameTaken(outStream)) {
                        this.output.println("Username already taken.");
                        continue;
                    }

                    username = outStream;
                    System.out.println(username + " registered.");
                    this.userList.put(username, this);
                    this.output.println("Welcome " + this.username);
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

    private void handleUserCommand(String message) {
        String[] splitMessage = message.split("\\s+");
        switch(splitMessage[0]) {
            case "v":
                StringBuilder users = new StringBuilder("");

                for (String user : this.userList.keySet()) {
                    users.append(user + "\n");
                }

                this.output.println(users.toString());

                break;
            case "w":
                String user = splitMessage[1];

                ServerCommunicationThread destinationUser = userList.get(user);
                if (destinationUser == null) {
                    this.output.println("User " + user + " does not exist.");
                } else {
                    int leftOffset = splitMessage[1].length() + 3;
                    String cleanMessage = message.substring(leftOffset);

                    destinationUser.output.println("[Whisper]" + username + ": " + cleanMessage);
                }
                break;
            default:
                System.out.println("Unknown command");
                this.output.println("Unknown command. Command list:\n/v - view active users\n/w <username> <message> - send private message");
                break;
        }
    }

    private boolean isUsernameTaken(String username) {
        if (userList.containsKey(username)) {
            return true;
        }
        return false;
    }
}
