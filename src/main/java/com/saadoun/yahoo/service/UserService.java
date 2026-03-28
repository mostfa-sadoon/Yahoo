package com.saadoun.yahoo.service;

import com.saadoun.yahoo.model.dto.RegisterRequest;
import com.saadoun.yahoo.model.entity.User;
import com.saadoun.yahoo.repository.UserRepositoryInterface;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepositoryInterface userRepositoryInterface;

    @Autowired
    PasswordEncoder passwordEncoder;

    public User save(RegisterRequest dto){

        if (userRepositoryInterface.existsByEmail(dto.getEmail())) {
            throw new com.saadoun.yahoo.exception.BadRequestException("Email already exists");
        }

        if (userRepositoryInterface.existsByUsername(dto.getUsername())) {
            throw new com.saadoun.yahoo.exception.BadRequestException("Username already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .first_name(dto.getFirst_name())
                .last_name(dto.getLast_name())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
        System.out.println(user.toString());
       return   userRepositoryInterface.save(user);
    }

    public List<User> getAllUsers(){
        return  userRepositoryInterface.findAll();
    }
}
