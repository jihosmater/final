package com.team.demo.domain.dto.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NaverCallback {
	private String code;
	private String state;
	private String error;
	private String error_description;
}
