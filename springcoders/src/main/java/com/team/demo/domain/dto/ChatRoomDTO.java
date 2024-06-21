package com.team.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {
	private int roomno;
	private String adminid;
    private String bannuser;
    private String roomname;
    private String roomtype;
}
