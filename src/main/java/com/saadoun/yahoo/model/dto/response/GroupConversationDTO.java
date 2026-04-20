package com.saadoun.yahoo.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupConversationDTO {
    private Long id;
    private String name;
    private long participantCount;
}
