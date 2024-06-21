package com.team.demo.domain.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Response {
	private String id;
	private String nickname;
	private String name;
	private String profile_image;
}
