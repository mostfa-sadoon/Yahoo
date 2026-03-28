package com.saadoun.yahoo.repository;

import com.saadoun.yahoo.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositoryInterface extends JpaRepository<User,Long> {
    public Optional<User> findByUsername(String UserName);
    public Optional<User>  findByEmail(String Email);

    public Boolean existsByEmail(String Email);
    public Boolean existsByUsername(String UserName);

}
