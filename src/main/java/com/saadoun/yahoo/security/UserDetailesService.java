package com.saadoun.yahoo.security;

import com.saadoun.yahoo.model.entity.User;
import com.saadoun.yahoo.repository.UserRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailesService implements UserDetailsService {

    @Autowired
    UserRepositoryInterface userRepositoryInterface;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user  = this.userRepositoryInterface.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
