package com.team.demo.domain.dto.login;

import lombok.Data;

@Data
public class KakaoInfo {
	private Long id;
    private String nickname;
    private String profileImage;

    public KakaoInfo(Long id, String nickname, String profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}