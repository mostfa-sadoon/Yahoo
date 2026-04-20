package com.saadoun.yahoo.service;

import com.saadoun.yahoo.model.dto.response.GroupConversationDTO;
import com.saadoun.yahoo.model.dto.response.PrivateConversationDTO;
import com.saadoun.yahoo.model.entity.Conversation;
import com.saadoun.yahoo.model.entity.ConversationParticipation;
import com.saadoun.yahoo.repository.ConversationParticipationRepositoryInterface;
import com.saadoun.yahoo.repository.ConversationRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ConversationService {

    @Autowired
    private ConversationParticipationRepositoryInterface participationRepository;

    @Autowired
    private ConversationRepositoryInterface conversationRepository;

    public List<PrivateConversationDTO> getPrivateConversations(Long userId) {
        return participationRepository.findPrivateConversationsForUser(userId);
    }

    public List<GroupConversationDTO> getGroupConversations(Long userId) {
        return participationRepository.findGroupConversationsForUser(userId);
    }

    public Conversation createGroupConversation(String name, List<Long> participantIds) {
        Conversation conversation = Conversation.builder()
                .name(name)
                .isGroup(true)
                .createdAt(LocalDateTime.now())
                .build();

        Conversation savedConversation = conversationRepository.save(conversation);

        for (Long userId : participantIds) {
            addParticipantToGroup(savedConversation.getId(), userId);
        }

        return savedConversation;
    }

    public void addParticipantToGroup(Long conversationId, Long userId) {
        if (!participationRepository.existsByConversationIdAndUserId(conversationId, userId)) {
            ConversationParticipation participation = ConversationParticipation.builder()
                    .conversationId(conversationId)
                    .userId(userId)
                    .joinedAt(LocalDateTime.now())
                    .build();
            participationRepository.save(participation);
        }
    }
}
