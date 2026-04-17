package com.saadoun.yahoo.controller;

import com.saadoun.yahoo.model.dto.LoginRequest;
import com.saadoun.yahoo.model.dto.RegisterRequest;
import com.saadoun.yahoo.model.dto.response.RegisterResponse;
import com.saadoun.yahoo.model.entity.User;
import com.saadoun.yahoo.repository.UserRepositoryInterface;
import com.saadoun.yahoo.service.UserService;
import com.saadoun.yahoo.util.ApiResponse;
import com.saadoun.yahoo.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("auth")
@RestController
public class AuthController {

   @Autowired
   AuthenticationManager authenticationManager;

   @Autowired
   UserService userService;

   @Autowired
   JwtUtil jwtUtil;

   @PostMapping("login")
   public ResponseEntity<ApiResponse<Token>> login(@RequestBody LoginRequest dto){
       System.out.println("user name ="+dto.getUsername());
       Authentication auth = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword())
       );
       if (!auth.isAuthenticated()) {
           throw new RuntimeException("Invalid login");
       }

       String token = jwtUtil.generateToken(dto.getUsername());
       Token jwtTokwn = new Token();
       jwtTokwn.token = token;
       ApiResponse<Token> response = new ApiResponse<Token>(
               "login success",
               jwtTokwn
       );
       return ResponseEntity.ok(response);
   }

   @PostMapping("register")
   public ResponseEntity<ApiResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request){
           User savedUser = userService.save(request);
           RegisterResponse regiser = RegisterResponse.builder()
                   .email(savedUser.getEmail())
                   .userName(savedUser.getUsername())
                   .id(savedUser.getId())
                   .build();
           ApiResponse<RegisterResponse> response =  new ApiResponse<RegisterResponse>(
                   "registration success",
                   regiser
           );
           return ResponseEntity.ok(response);
   }

    class Token{
        public String token;
    }

}
