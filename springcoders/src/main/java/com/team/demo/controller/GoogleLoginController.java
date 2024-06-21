package com.team.demo.controller;

import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.team.demo.domain.dto.UserDTO;
import com.team.demo.domain.dto.login.GoogleLoginResponse;
import com.team.demo.domain.dto.login.GoogleOAuthReqest;
import com.team.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.net.URI;

@Slf4j
@Controller
public class GoogleLoginController {
    @Value("${google.auth.url}")
    private String googleAuthUrl;

    @Value("${google.login.url}")
    private String googleLoginUrl;

    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.redirect.url}")
    private String googleRedirectUrl;

    @Value("${google.secret}")
    private String googleClientSecret;

    @Autowired
    private UserService userService;

    // 구글 로그인창 호출
    // http://localhost:8080/login/getGoogleAuthUrl
    @GetMapping(value = "/googlelogin/getGoogleAuthUrl")
    public ResponseEntity<?> getGoogleAuthUrl(HttpServletRequest request) throws Exception {

        String reqUrl = googleLoginUrl + "/o/oauth2/v2/auth?client_id=" + googleClientId + "&redirect_uri=" + googleRedirectUrl
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";

        log.info("myLog-LoginUrl : {}",googleLoginUrl);
        log.info("myLog-ClientId : {}",googleClientId);
        log.info("myLog-RedirectUrl : {}",googleRedirectUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(reqUrl));

        //1.reqUrl 구글로그인 창을 띄우고, 로그인 후 /login/oauth_google_check 으로 리다이렉션하게 한다.
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    // 구글에서 리다이렉션
    @GetMapping(value = "/login/oauth_google_check")
    public String oauth_google_check(HttpServletRequest request,
                                     @RequestParam(value = "code") String authCode,
                                     HttpServletResponse response, HttpSession session, Model model) throws Exception{

        //2.구글에 등록된 레드망고 설정정보를 보내어 약속된 토큰을 받위한 객체 생성
        GoogleOAuthReqest googleOAuthRequest = GoogleOAuthReqest
                .builder()
                .clientId(googleClientId)
                .clientSecret(googleClientSecret)
                .code(authCode)
                .redirectUri(googleRedirectUrl)
                .grantType("authorization_code")
                .build();

        RestTemplate restTemplate = new RestTemplate();

        //3.토큰요청을 한다.
        ResponseEntity<GoogleLoginResponse> apiResponse = restTemplate.postForEntity(googleAuthUrl + "/token", googleOAuthRequest, GoogleLoginResponse.class);
        //4.받은 토큰을 토큰객체에 저장
        GoogleLoginResponse googleLoginResponse = apiResponse.getBody();

        log.info("responseBody {}",googleLoginResponse.toString());


        String googleToken = googleLoginResponse.getId_token();

        //5.받은 토큰을 구글에 보내 유저정보를 얻는다.
        String requestUrl = UriComponentsBuilder.fromHttpUrl(googleAuthUrl + "/tokeninfo").queryParam("id_token",googleToken).toUriString();

        //6.허가된 토큰의 유저정보를 결과로 받는다.
        String resultJson = restTemplate.getForObject(requestUrl, String.class);

        JSONParser jsonParse = new JSONParser();
        JSONObject obj =  (JSONObject)jsonParse.parse(resultJson);

        String userid = "GOOGLE"+obj.get("sub");
        
        String givenName = obj.get("given_name") != null ? obj.get("given_name").toString() : "";
        String familyName = obj.get("family_name") != null ? obj.get("family_name").toString() : "";
        String username = givenName + (givenName.isEmpty() ? "" : " ") + familyName;

        String userpw = userService.randpass();
        UserDTO user = new UserDTO(userid, userpw, username);

        if(userService.checkId(userid)!=null) {
            session.setAttribute("loginUser", userid);
           System.out.println(session.getAttribute("loginUser"));
           session.setAttribute("loginName", username);
            // session.setMaxInactiveInterval( ) : 세션 타임아웃을 설정하는 메서드
            // 로그인 유지 시간 설정 (3600초 == 60분 60분 * 6 총 6시간)
            session.setMaxInactiveInterval(60 * 60 * 6);
        }
        else {
            System.out.println(user);
            
            model.addAttribute("userid", user.getUserid());
            model.addAttribute("userpw", user.getUserpw());
            model.addAttribute("username", user.getUsername());
            return "/user/apijoin";
        }

        return "redirect:/";
    }
    @GetMapping("/googlelogout")
    public String logout(HttpSession session) {
        return "redirect:/user/login";
    }
}