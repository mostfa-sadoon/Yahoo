package com.saadoun.yahoo.repository;

import com.saadoun.yahoo.Enums.FriendRequestStatus;
import com.saadoun.yahoo.model.entity.FriendRequest;
import com.saadoun.yahoo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestRepositoryInterface extends JpaRepository<FriendRequest, Long> {
    
    Optional<FriendRequest> findBySenderAndReceiver(User sender, User receiver);
    
    boolean existsBySenderAndReceiver(User sender, User receiver);
    
    java.util.List<FriendRequest> findByReceiverAndStatus(User receiver, FriendRequestStatus status);
}
