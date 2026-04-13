package com.saadoun.yahoo.repository;

import com.saadoun.yahoo.model.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepositoryInterface extends JpaRepository <Conversation ,Long> {
}
