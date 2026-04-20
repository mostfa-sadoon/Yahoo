package com.saadoun.yahoo.controller.web;

import com.saadoun.yahoo.model.dto.response.FriendRequestResponseDTO;
import com.saadoun.yahoo.model.dto.response.UserResponseDTO;
import com.saadoun.yahoo.service.FriendRequestService;
import com.saadoun.yahoo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chat-api")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private FriendRequestService friendRequestService;

    @GetMapping("/users/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam("keyword") String keyword) {
        List<UserResponseDTO> users = userService.searchUsers(keyword);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/friends/request")
    public ResponseEntity<?> sendFriendRequest(@RequestParam("receiverUsername") String receiverUsername) {
        try {
            friendRequestService.sendFriendRequest(receiverUsername);
            return ResponseEntity.ok(Map.of("message", "Friend request sent successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/friends/pending")
    public ResponseEntity<List<FriendRequestResponseDTO>> getPendingRequests() {
        return ResponseEntity.ok(friendRequestService.getPendingRequests());
    }

    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriendRequest(@RequestParam("requestId") Long requestId) {
        try {
            friendRequestService.acceptFriendRequest(requestId);
            return ResponseEntity.ok(Map.of("message", "Friend request accepted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/friends/reject")
    public ResponseEntity<?> rejectFriendRequest(@RequestParam("requestId") Long requestId) {
        try {
            friendRequestService.rejectFriendRequest(requestId);
            return ResponseEntity.ok(Map.of("message", "Friend request rejected"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/friends")
    public ResponseEntity<List<UserResponseDTO>> getFriends() {
        return ResponseEntity.ok(friendRequestService.getFriends());
    }
}
