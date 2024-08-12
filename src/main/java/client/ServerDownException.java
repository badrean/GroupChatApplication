package client;

public class ServerDownException extends Exception {
    public ServerDownException() {}

    public ServerDownException(String message)
    {
        super(message);
    }
}
