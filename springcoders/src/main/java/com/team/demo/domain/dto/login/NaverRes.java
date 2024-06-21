package com.team.demo.domain.dto.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class NaverRes {
	private String resultcode;
	private String message;
	private Response response;
}
