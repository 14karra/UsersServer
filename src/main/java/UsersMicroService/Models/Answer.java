package UsersMicroService.Models;

import UsersMicroService.classes.Client;

public class Answer
{
    private String status;
    private String Details;
    private Client client;

    public Answer(String status, String details, Client client) {
        this.status = status;
        Details = details;
        this.client = client;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
