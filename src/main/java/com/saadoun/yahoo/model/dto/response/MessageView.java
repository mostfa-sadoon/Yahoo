package com.saadoun.yahoo.model.dto.response;

import java.time.LocalDateTime;

public interface MessageView {
    String getContent();
    Long getSenderId();
    Long getReceiverId();
    LocalDateTime getCreatedAt();
    Long getConversationId();
}