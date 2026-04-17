package com.saadoun.yahoo.controller.web;

import com.saadoun.yahoo.model.dto.response.PrivateConversationDTO;
import com.saadoun.yahoo.security.CustomUserDetails;
import com.saadoun.yahoo.security.UserDetailesService;
import com.saadoun.yahoo.service.ConversationService;
import com.saadoun.yahoo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    ConversationService conversationService;

   @GetMapping("/")
    public String home(Model model , @AuthenticationPrincipal CustomUserDetails userDetails){
       Long authUserId = userDetails.getId();
       List<PrivateConversationDTO> privateConversations = conversationService.getPrivateConversations(authUserId);
       model.addAttribute("users",userService.getAllUsers());
       model.addAttribute("privateConversations",privateConversations);
       model.addAttribute("user_id",authUserId);
       return "chat";
   }

}
