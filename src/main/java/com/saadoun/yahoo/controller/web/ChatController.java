package com.saadoun.yahoo.controller.web;

import com.saadoun.yahoo.model.dto.MessageDTO;
import com.saadoun.yahoo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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
    public  void sendMessage(MessageDTO message){

        messageService.save(message);

        if(message.isGroup()){
            messagingTemplate.convertAndSend(
                    "/topic/conversation/" + message.getConversationId(),
                    message
            );
        }else{
            // send to receiver
            messagingTemplate.convertAndSendToUser(
                    message.getReceiverUsername(),
                    "/queue/messages",
                    message
            );

            // send back to sender (sync UI)
            messagingTemplate.convertAndSendToUser(
                    message.getSenderUsername(),
                    "/queue/messages",
                    message
            );
        }
    }








}
