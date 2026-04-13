package com.saadoun.yahoo.service;

import com.saadoun.yahoo.model.dto.MessageDTO;
import com.saadoun.yahoo.model.entity.Conversation;
import com.saadoun.yahoo.model.entity.Message;
import com.saadoun.yahoo.model.entity.User;
import com.saadoun.yahoo.repository.ConversationRepositoryInterface;
import com.saadoun.yahoo.repository.MessageRepositoryInterface;
import com.saadoun.yahoo.repository.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MessageService {

   @Autowired
    MessageRepositoryInterface messageRepositoryInterface;

   @Autowired
    UserRepositoryInterface userRepositoryInterface;

   @Autowired
    ConversationRepositoryInterface conversationRepositoryInterface;

   public Message save(MessageDTO dto){
       System.out.println("string of dto"+dto.toString());
       User sender = userRepositoryInterface.findByUsername(dto.getSenderUsername()).orElseThrow();
       User reciver = userRepositoryInterface.findByUsername(dto.getReceiverUsername()).orElseThrow();
    //   Conversation conversation = conversationRepositoryInterface.findById(dto.getConversationId()).orElseThrow();
       Message message = Message.builder()
               .content(dto.getContent())
               .senderId(sender.getId())
               .receiverId(reciver.getId())
             //  .conversationId(dto.getConversationId())
               .createdAt(LocalDateTime.now())
               .build();
       Message saveedMessage = messageRepositoryInterface.save(message);
       return  saveedMessage;
   }


}
