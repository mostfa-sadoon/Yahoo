package com.saadoun.yahoo.service;

import com.saadoun.yahoo.model.dto.response.PrivateConversationDTO;
import com.saadoun.yahoo.repository.ConversationParticipationRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {

   @Autowired
    ConversationParticipationRepositoryInterface participationRepository;

   public List<PrivateConversationDTO> getPrivateConversations(Long userId){
       return participationRepository.findPrivateConversationsForUser(userId);
   }

}
