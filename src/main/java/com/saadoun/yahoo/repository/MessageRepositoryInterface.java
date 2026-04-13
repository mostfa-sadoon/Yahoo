package com.saadoun.yahoo.repository;

import com.saadoun.yahoo.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepositoryInterface extends JpaRepository<Message,Long> {
}
