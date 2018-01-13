package fr.aireisti.aircontest.jobs;

public class WorkerMessage {
    private String message;
    private String messageType;


    public WorkerMessage(String messageType, String message) {
        this.message = message;
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
