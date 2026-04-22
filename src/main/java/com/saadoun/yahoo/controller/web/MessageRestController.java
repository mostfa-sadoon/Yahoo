package com.saadoun.yahoo.controller.web;

import com.saadoun.yahoo.model.dto.response.MessageView;
import com.saadoun.yahoo.model.dto.response.MessageViewDto;
import com.saadoun.yahoo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageRestController {

    @Autowired
    MessageService messageService;

    @GetMapping("messages")
    public List<MessageViewDto> getMessages(@RequestParam Long conversationId){
        return  messageService.getConversationMessages(conversationId);
    }


}
