package client;

import java.io.IOException;

import client.controllers.ClientMessages;
import common.controllers.Message;
import common.entity.User;
import common.ocsf.client.AbstractClient;

/**
 * The Client class extends AbstractClient represents the fulfill of client model. taken from lab06.
 *
 * @author names
 */
public class Client extends AbstractClient {
    /**
     * all of the below are objects of each client controllers that we created
     */
    public static User user;


    /**
     * Client is execute the connection to the server
     *
     * @param host is the host ip number
     * @param port is the port number
     * @throws IOException if IO problems occurs
     */
    public Client(String host, int port) throws IOException {
        super(host, port);
        openConnection();
    }

    /**
     * handleMessageFromServer is handling the respond from the server due to client request
     *
     * @param msg is the message that received from the server
     */
    @Override
    protected void handleMessageFromServer(Object msg) {
        //System.out.println("--> handleMessageFromServer");
        try {
            ClientMessages.messageFromServer((Message) msg);
        } catch (Exception e) {
            e.printStackTrace();

            System.out.println("Could not handle message from server. " + e);
        }

    }

    /**
     * handleMessageFromClientUI is channeling the client's request to the server
     *
     * @param message is the message that need to be sent to the server
     */
    public void handleMessageFromClientUI(Object message) {
        try {
            sendToServer(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not send message to server.  Terminating client." + e);
        }
    }

}