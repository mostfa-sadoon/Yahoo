package com.saadoun.yahoo.service;

import com.saadoun.yahoo.model.dto.MessageDTO;
import com.saadoun.yahoo.model.dto.response.MessageView;
import com.saadoun.yahoo.model.dto.response.MessageViewDto;
import com.saadoun.yahoo.model.entity.*;
import com.saadoun.yahoo.repository.*;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.security.Principal;
import java.time.Duration;
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

   @Autowired
   private StringRedisTemplate redisTemplate;

   @Autowired
   private ObjectMapper objectMapper;

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
               .isGroup(dto.isGroup())
               .build();
       Message saveedMessage = messageRepositoryInterface.save(message);
       // update redis
       try{
           String key = "chat:messages" + dto.getConversationId();
           String cached = redisTemplate.opsForValue().get(key);
           if(cached!=null){
               List<MessageViewDto> messages = objectMapper.readValue(
                       cached,
                       objectMapper.getTypeFactory()
                               .constructCollectionType(List.class,MessageViewDto.class)
               );

               MessageViewDto newMessage = MessageViewDto.builder()
                       .content(saveedMessage.getContent())
                       .receiverId(saveedMessage.getReceiverId())
                       .senderId(saveedMessage.getSenderId())
                       .createdAt(saveedMessage.getCreatedAt())
                       .build();
               messages.add(newMessage);
               // limit cache size with 50 messages only
               if (messages.size() > 50) {
                   messages = messages.subList(messages.size() - 50, messages.size());
               }
               redisTemplate.opsForValue().set(key,
                       objectMapper.writeValueAsString(messages),
                       Duration.ofMinutes(10));
           }


       } catch (RuntimeException e) {
           throw new RuntimeException("Redis Update Field",e);
       }

       return  saveedMessage;
   }

   public List<MessageViewDto> getConversationMessages(Long conversationId){
       String key = "chat:messages" + conversationId;
       try {
           // get data from redis
           String cached = redisTemplate.opsForValue().get(key);
           if(cached!=null){
               log.info("loaded messages from redis");
               return objectMapper.readValue(
                       cached,
                       objectMapper.getTypeFactory()
                               .constructCollectionType(List.class, MessageViewDto.class)
               );
           }
           // fall back to database
           log.info("loaded messages from db");
           List<MessageViewDto> messages =  messageRepositoryInterface.getConversationMessages(conversationId);
           redisTemplate.opsForValue().set(
                   key,
                   objectMapper.writeValueAsString(messages),
                   Duration.ofMinutes(10) // TTL
           );
           return messages;
       } catch (RuntimeException e) {
           throw new RuntimeException("ERROR hndeling Redis chache",e);
       }
   }

}
