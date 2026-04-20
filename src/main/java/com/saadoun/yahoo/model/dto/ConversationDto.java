package com.saadoun.yahoo.model.dto;

import lombok.Data;

import java.util.List;


@Data
public class ConversationDto {

    private String name;
    private List<Integer> participantIds;

}
