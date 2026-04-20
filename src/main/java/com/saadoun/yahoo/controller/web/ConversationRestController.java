package com.saadoun.yahoo.controller.web;

import com.saadoun.yahoo.model.dto.ConversationDto;
import com.saadoun.yahoo.model.entity.Conversation;
import com.saadoun.yahoo.service.ConversationService;
import com.saadoun.yahoo.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat-api/groups")
public class ConversationRestController {

    @Autowired
    private ConversationService conversationService;

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody ConversationDto payload,
                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        String name =  payload.getName();
        List<Integer> participantIdsInt = payload.getParticipantIds();
        
        List<Long> participantIds = participantIdsInt.stream()
                .map(Integer::longValue)
                .collect(java.util.stream.Collectors.toList());

        // Add creator to group if not already in list
        if (!participantIds.contains(userDetails.getId())) {
            participantIds.add(userDetails.getId());
        }

        Conversation conversation = conversationService.createGroupConversation(name, participantIds);
        return ResponseEntity.ok(conversation);
    }

    @PostMapping("/{id}/participants")
    public ResponseEntity<?> addParticipant(@PathVariable Long id, @RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        conversationService.addParticipantToGroup(id, userId);
        return ResponseEntity.ok().build();
    }
}
