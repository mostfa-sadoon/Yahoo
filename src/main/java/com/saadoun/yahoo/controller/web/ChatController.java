package com.saadoun.yahoo.controller.web;

import com.saadoun.yahoo.model.dto.MessageDTO;
import com.saadoun.yahoo.model.dto.response.MessageView;
import com.saadoun.yahoo.security.CustomUserDetails;
import com.saadoun.yahoo.service.MessageService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

    @Autowired
     SimpMessagingTemplate messagingTemplate;

    @Autowired
    MessageService messageService;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(Message message) {
        return message; // later: save to DB
    }


    @MessageMapping("/chat.send")
    public  void sendMessage(MessageDTO message, SimpMessageHeaderAccessor headerAccessor){

        System.out.println("message is sent success");
        System.out.println("Message dto is"+ message.toString());

        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        Long userId = (Long) sessionAttributes.get("userId");
        com.saadoun.yahoo.model.entity.Message messageentity =  messageService.save(message,userId);

        if(message.isGroup()){

            messagingTemplate.convertAndSend(
                    "/topic/conversation/" + message.getConversationId(),
                    messageentity
            );
        }else{
            // send to receiver
            messagingTemplate.convertAndSendToUser(
                    message.getReceiverUsername(),
                    "/queue/messages",
                    messageentity
            );

            // send back to sender (sync UI)
            messagingTemplate.convertAndSendToUser(
                    message.getSenderUsername(),
                    "/queue/messages",
                    messageentity
            );
        }
    }

}
