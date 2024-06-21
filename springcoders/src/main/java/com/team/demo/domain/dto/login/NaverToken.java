package com.team.demo.domain.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NaverToken {
	 private String access_token;
	 private String refresh_token;
     private String token_type;
     private int expires_in;
     private String error;
     private String error_description;
}
