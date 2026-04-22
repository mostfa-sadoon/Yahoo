package com.saadoun.yahoo.model.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageViewDto {

    private String content;
    private Long senderId;
    private Long receiverId;
    private LocalDateTime createdAt;
    private Long conversationId;
}