package UsersMicroService.Models;

public class Answer
{
    private String status;
    private String Details;

    public Answer(String status, String details) {
        this.status = status;
        Details = details;
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
}
