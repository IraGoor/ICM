package server;

import client.Client;

public class ConnectedUser {
    private String username;
    private Client client;

    public ConnectedUser(String username, Client client) {
        this.username = username;
        this.client = client;
    }

    public String getUsername() {
        return username;
    }

    public Client getClient() {
        return client;
    }
}


