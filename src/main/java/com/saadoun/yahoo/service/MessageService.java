package com.saadoun.yahoo.service;

import com.saadoun.yahoo.model.dto.MessageDTO;
import com.saadoun.yahoo.model.dto.response.MessageView;
import com.saadoun.yahoo.model.entity.*;
import com.saadoun.yahoo.repository.*;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MessageService {

   @Autowired
    MessageRepositoryInterface messageRepositoryInterface;

   @Autowired
    UserRepositoryInterface userRepositoryInterface;

   @Autowired
    ConversationRepositoryInterface conversationRepositoryInterface;

   @Autowired
   ConversationParticipationRepositoryInterface convParticipationRepository;

   public Message save(MessageDTO dto, Long UserId){
       System.out.println("string of dto"+dto.toString());

       boolean  exists = convParticipationRepository
               .existsByConversationIdAndUserId(dto.getConversationId(), UserId);

       if (!exists) {
           throw new RuntimeException("User is not allowed to send message in this conversation");
       }

       Message message = Message.builder()
               .content(dto.getContent())
               .senderId(UserId)
               .receiverId(dto.getReceiverId())
               .conversationId(dto.getConversationId())
               .createdAt(LocalDateTime.now())
               .build();
       Message saveedMessage = messageRepositoryInterface.save(message);
       return  saveedMessage;
   }

   public List<MessageView> getConversationMessages(Long conversationId){
            return messageRepositoryInterface.getConversationMessages(conversationId);
   }

}
