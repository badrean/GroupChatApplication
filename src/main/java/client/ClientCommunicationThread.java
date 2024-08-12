package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientCommunicationThread extends Thread {
    static boolean usernameRegistered = false;
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
                communicationStatus = true;
                notifyAll();
            }

            while (this.communicationStatus) {
                String incomingMessage = input.readLine();

                if (incomingMessage == null) {
                    communicationStatus = false;
                    throw new ServerDownException("Server lost connection");
                }

                System.out.println(incomingMessage);
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
        this.communicationStatus = false;
    }

    public synchronized boolean getCommunicationStatus() {
        return this.communicationStatus;
    }
}
