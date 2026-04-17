package com.saadoun.yahoo.repository;

import com.saadoun.yahoo.model.dto.MessageDTO;
import com.saadoun.yahoo.model.dto.response.MessageView;
import com.saadoun.yahoo.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepositoryInterface extends JpaRepository<Message,Long> {

    @Query("""
        SELECT m.content         AS content,
               m.senderId        AS senderId,
               m.receiverId      AS receiverId,
               m.createdAt       AS createdAt,
               m.conversationId  AS conversationId
        FROM Message m
        WHERE m.conversationId = :conversationId
        ORDER BY m.createdAt ASC
    """)
    public List<MessageView> getConversationMessages(Long conversationId);
}
