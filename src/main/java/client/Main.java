package client;

public class Main {
    public static void main(String[] args) {
        try {
            Client client = new Client("127.0.0.1", 5000);
            client.runClient();
        } catch (Exception e) {
            System.out.println("Client Error: " + e.getMessage());
        }
    }
}