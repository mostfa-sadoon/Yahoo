package com.saadoun.yahoo.model.dto;

import lombok.Data;

@Data
public class MessageDTO {
    private Long conversationId;
    private boolean Group;
    private String content;
    private String sender;
    private Long senderId;
    private String senderUsername;
    // for private chat only
    private Long receiverId;
    private String receiverUsername;
}
