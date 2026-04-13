package com.saadoun.yahoo.service;

import com.saadoun.yahoo.model.entity.Conversation;
import com.saadoun.yahoo.model.entity.ConversationParticipation;
import com.saadoun.yahoo.model.entity.FriendRequest;
import com.saadoun.yahoo.Enums.FriendRequestStatus;
import com.saadoun.yahoo.model.entity.User;
import com.saadoun.yahoo.repository.ConversationParticipationRepositoryInterface;
import com.saadoun.yahoo.repository.ConversationRepositoryInterface;
import com.saadoun.yahoo.repository.FriendRequestRepositoryInterface;
import com.saadoun.yahoo.repository.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.saadoun.yahoo.model.dto.response.FriendRequestResponseDTO;
import com.saadoun.yahoo.model.dto.response.UserResponseDTO;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;


@Service
@Transactional
@Slf4j
public class FriendRequestService {

    @Autowired
    private FriendRequestRepositoryInterface friendRequestRepository;

    @Autowired
    private UserRepositoryInterface userRepository;

    @Autowired
    private ConversationRepositoryInterface conversationRepositoryInterface;

    @Autowired
    private ConversationParticipationRepositoryInterface conversationParticipationRepositoryInterface;

    public void sendFriendRequest(String receiverUsername) {
        String currentUsername = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        if (currentUsername.equals(receiverUsername)) {
            throw new IllegalArgumentException("Cannot send friend request to yourself");
        }

        User sender = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
                
        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (friendRequestRepository.existsBySenderAndReceiver(sender, receiver) || 
            friendRequestRepository.existsBySenderAndReceiver(receiver, sender)) {
            throw new IllegalStateException("Friend request already exists");
        }

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build();

        friendRequestRepository.save(friendRequest);

    }

    public List<FriendRequestResponseDTO> getPendingRequests() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User receiver = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("Fetching pending requests for user: {}", currentUsername);
        List<FriendRequest> requests = friendRequestRepository.findByReceiverAndStatus(receiver, FriendRequestStatus.PENDING);
        log.info("Found {} pending requests for user: {}", requests.size(), currentUsername);

        return requests.stream().map(req -> {
            User sender = req.getSender();
            UserResponseDTO senderDto = UserResponseDTO.builder()
                    .id(sender.getId())
                    .username(sender.getUsername())
                    .firstName(sender.getFirst_name())
                    .lastName(sender.getLast_name())
                    .build();

            return FriendRequestResponseDTO.builder()
                    .id(req.getId())
                    .sender(senderDto)
                    .build();
        }).collect(Collectors.toList());
    }

    public void acceptFriendRequest(Long requestId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (!request.getReceiver().getUsername().equals(currentUsername)) {
            throw new RuntimeException("Unauthorized to accept this request");
        }

        request.setStatus(FriendRequestStatus.ACCEPTED);
        friendRequestRepository.save(request);

        // create conversation
        Conversation conversation  = Conversation.builder()
                .name(null)
                .createdAt(LocalDateTime.now())
                .build();

        Conversation saveconversation = conversationRepositoryInterface.save(conversation);

        ConversationParticipation conversationParticipation1 = ConversationParticipation.builder()
                .conversationId(saveconversation.getId())
                .userId(request.getSender().getId())
                .build();

        conversationParticipationRepositoryInterface.save(conversationParticipation1);

        ConversationParticipation conversationParticipation2 = ConversationParticipation.builder()
                .conversationId(saveconversation.getId())
                .userId(request.getReceiver().getId())
                .build();

        conversationParticipationRepositoryInterface.save(conversationParticipation2);

    }

    public void rejectFriendRequest(Long requestId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        if (!request.getReceiver().getUsername().equals(currentUsername)) {
            throw new RuntimeException("Unauthorized to reject this request");
        }

        friendRequestRepository.delete(request);
    }
}
