package com.team.demo.domain.dto;

import java.text.DecimalFormat;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Data;

@Data
public class UserDTO {
    private String userid;
    private String userpw;
    private String username;
    private String userphone;
    private String useremail;
    private String usergender;
    private String zipcode;
    private String addr;
    private String addrdetail;
    private String addretc;
    private String userprofile;
    private int usercoin;
    private String category;
    
    public UserDTO(String userid, String userpw, String username) {
    	this.userid = userid;
    	this.userpw = userpw;
    	this.username = username;
    }
    
    public String getListLink() {							// ? 앞에 붙는 URI 문자열
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("userid", userid)
				.queryParam("userpw", userpw)
				.queryParam("username", username);
		return builder.toUriString();
	}
    
    public String getChangeusercoin() {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(usercoin);
    }
    
}

