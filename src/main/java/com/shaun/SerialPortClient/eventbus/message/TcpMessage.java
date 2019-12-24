package com.shaun.SerialPortClient.eventbus.message;

public class TcpMessage {
    private String messageType;
    private String messageContent;

    public TcpMessage(String messageType, String messageContent) {
        this.messageType = messageType;
        this.messageContent = messageContent;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}