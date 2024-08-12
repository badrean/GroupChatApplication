# Group Chat Application
## Introduction
This is a simple terminal group chat application written in Java. The application allows multiple users to connect and communicate in real-time through the terminal. Each user is identified by a unique username, and all messages are broadcast to every connected user. The application supports private messaging and a command to view online users.

## Features
* **Username Registration:** Users must enter a unique username upon starting the application. The system checks if the username is already in use to prevent duplicates.
* **Real-Time Messaging:** Messages typed by any user are instantly broadcast to all connected users.
* **Command Support:**
  * /v: Displays a list of all currently online users.
  * /w _username_ _message: Sends a private message to a specified user. Only the recipient can see this message.
* **Server Down Notification:** If the server crashes, all connected users receive a notification before the application exits.

## Installation

1. <b>Clone the repository:</b>
  ```
  git clone git@github.com:badrean/GroupChatApplication.git
  cd GroupChatApplication
  ```

2. <b>Build and Run the Application:</b>
   * Compile the Project:
     ```
       mvn clean compile
     ```
   * Run the Application:
     * Server:
     ```
       mvn exec:java -Dexec.mainClass="server.Main"
     ```
     * Client:
     ```
       mvn exec:java -Dexec.mainClass="client.Main"
     ```

## Usage
* **Server:** Start the server and wait for client connections.
* **Client:** Run one or more clients from different terminals and explore the features.

## Future Enhancements
* **Persistent Storage:** Implement persistent storage for users and conversations to maintain chat history and user data.
* **GUI:** Develop a GUI for the application to make it more user-friendly.

## Contributing
Contributions are welcome!

## License
This project is licensed under the MIT License - see the [LICENSE](https://github.com/badrean/GroupChatApplication/blob/main/LICENSE) file for details.
