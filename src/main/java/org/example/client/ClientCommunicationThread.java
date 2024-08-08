package org.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientCommunicationThread extends Thread {
    private Socket socket;
    private BufferedReader input;

    private boolean communicationStatus = false;

    public ClientCommunicationThread(Socket socket) throws IOException {
        this.socket = socket;
        input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            synchronized (this) {
                System.out.println("Started communication");
                communicationStatus = true;
                notifyAll();
            }

            while (communicationStatus) {
                System.out.println("Iteration in");
                String incomingMessage = input.readLine();

                if (incomingMessage == null) {
                    communicationStatus = false;
                    throw new ServerDownException("Server lost connection");
                }
            }
        } catch (ServerDownException e) {
            System.out.println("Server is down. Exiting...");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                input.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public synchronized void stopCommunication() {
        communicationStatus = false;
    }

    public synchronized boolean getCommunicationStatus() {
        return this.communicationStatus;
    }
}
