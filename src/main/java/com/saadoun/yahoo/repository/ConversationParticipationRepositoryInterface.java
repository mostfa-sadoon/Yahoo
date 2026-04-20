package com.saadoun.yahoo.repository;

import com.saadoun.yahoo.model.dto.response.GroupConversationDTO;
import com.saadoun.yahoo.model.dto.response.PrivateConversationDTO;
import com.saadoun.yahoo.model.entity.ConversationParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationParticipationRepositoryInterface extends JpaRepository<ConversationParticipation, Long> {


   @Query("""
           SELECT new com.saadoun.yahoo.model.dto.response.PrivateConversationDTO(
                              c.id,
                              u.id,
                              u.first_name,
                              u.last_name,
                              u.username
                          )
                           FROM ConversationParticipation myP
                                  JOIN Conversation c ON c.id = myP.conversationId
                                  JOIN ConversationParticipation otherP ON otherP.conversationId = myP.conversationId
                                  JOIN User u ON u.id = otherP.userId
                                  WHERE myP.userId = :authUserId
                                    AND c.isGroup = false
                                    AND otherP.userId != :authUserId
           """)
    List<PrivateConversationDTO> findPrivateConversationsForUser(@Param("authUserId") Long authIserId);

    @Query("""
            SELECT new com.saadoun.yahoo.model.dto.response.GroupConversationDTO(
                               c.id,
                               c.name,
                               (SELECT COUNT(p2) FROM ConversationParticipation p2 WHERE p2.conversationId = c.id)
                           )
                            FROM ConversationParticipation p
                                   JOIN Conversation c ON c.id = p.conversationId
                                   WHERE p.userId = :authUserId
                                     AND c.isGroup = true
            """)
    List<GroupConversationDTO> findGroupConversationsForUser(@Param("authUserId") Long authUserId);

    boolean existsByConversationIdAndUserId(Long conversationId, Long userId);
}
