package com.team.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserDTO {
	private int roomno;
	private String roomname;
	private String userid;
	private int last_read_chatno;
}
