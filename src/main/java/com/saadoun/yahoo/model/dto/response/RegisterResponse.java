package com.saadoun.yahoo.model.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class RegisterResponse {
    Long   id;
    String userName;
    String email;
    String Token;
}
