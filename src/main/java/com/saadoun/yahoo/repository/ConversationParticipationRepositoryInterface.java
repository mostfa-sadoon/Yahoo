package com.saadoun.yahoo.repository;

import com.saadoun.yahoo.model.entity.ConversationParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationParticipationRepositoryInterface extends JpaRepository<ConversationParticipation, Long> {
}
