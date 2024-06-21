package com.team.demo.domain.dto.login;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleOAuthReqest {
    private String redirectUri;
    private String clientId;
    private String clientSecret;
    private String code;
    private String responseType;
    private String scope;
    private String accessType;
    private String grantType;
    private String state;
    private String includeGrantedScopes;
    private String loginHint;
    private String prompt;
}