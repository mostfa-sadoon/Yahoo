package com.saadoun.yahoo.model.dto.response;

public record PrivateConversationDTO(
        Long conversationId,
        Long otherUserId,
        String otherUserFirstName,
        String otherUserLastName,
        String otherUserUsername
) {
}
