package com.team.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private int chatno;
    private String type;     
    private int roomno;            
    private String userid;          
    private String message;            
    private String time;               
}
