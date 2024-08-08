package org.example.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private String address = "";
    private String username = "";
    private int port;
    private Socket socket;
    private PrintWriter output;
    private Scanner input;
    private ClientCommunicationThread listenerThread;

    public Client(String address, int port) throws UnknownHostException, IOException {
        this.address = address;
        this.port = port;
        socket = new Socket(this.address, this.port);
        this.input = new Scanner(System.in);
        this.output = new PrintWriter(this.socket.getOutputStream(), true);
    }

    public void runClient() {
        try {
            do {
                System.out.print("Enter username: ");
                this.username = input.nextLine();
                output.println(this.username);
            } while(this.username.equals(""));

            listenerThread = new ClientCommunicationThread(socket);
            listenerThread.start();

            synchronized (listenerThread) {
                while (!listenerThread.getCommunicationStatus()) {
                    try {
                        listenerThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            while (listenerThread.getCommunicationStatus()) {
                String userInput = input.nextLine();
                output.println(userInput);

                if (userInput.toLowerCase().equals("exit")) {
                    listenerThread.stopCommunication();
                    break;
                }
            }

        } catch (IOException e) {
            System.out.println("I/O exception: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
