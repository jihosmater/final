package com.team.demo.controller;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.team.demo.domain.dto.UserDTO;
import com.team.demo.domain.dto.login.KakaoInfo;
import com.team.demo.service.KakaoService;
import com.team.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class KakaoLoginController {
	
	@Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;
    
    private final KakaoService kakaoService;
    
    @Autowired
	private UserService userService;

    @GetMapping(value = "/kakaologin/getKakaoAuthUrl")
    public ResponseEntity<?> getGoogleAuthUrl(HttpServletRequest request) throws Exception {

        String reqUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(reqUrl));

        //1.reqUrl 구글로그인 창을 띄우고, 로그인 후 /login/oauth_google_check 으로 리다이렉션하게 한다.
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
    
    
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, HttpSession session, Model model) throws IOException {
        String accessToken = kakaoService.getAccessTokenFromKakao(client_id, code);
        KakaoInfo kakaoInfo = null;
        kakaoInfo = kakaoService.getUserInfo(accessToken);
//        System.out.println(accessToken);
String userid = "KAKAO"+kakaoInfo.getId();
String username = kakaoInfo.getNickname();
String userpw = userService.randpass();
        
        if(!(userService.checkId(userid)==null)) {
        	UserDTO user = userService.checkId(userid);
        	System.out.println("KAKAO"+kakaoInfo.getId());
        	System.out.println("카카오 보내는 아이디" +userid);
        	session.setAttribute("loginUser", userid);
            // session.setMaxInactiveInterval( ) : 세션 타임아웃을 설정하는 메서드
            // 로그인 유지 시간 설정 (3600초 == 60분 60분 * 6 총 6시간)
            session.setMaxInactiveInterval(60 * 60 * 6);
            // 로그아웃 시 사용할 카카오토큰 추가
            session.setAttribute("kakaoToken", accessToken);
            session.setAttribute("loginName", username);
            System.out.println(session.getAttribute("kakaoToken"));
            
        }
        else {
            // String useremail = kakaoInfo
        	UserDTO user = new UserDTO(userid, userpw, username);
            System.out.println(user);
        	model.addAttribute("userid", user.getUserid());
        	model.addAttribute("userpw", user.getUserpw());
        	model.addAttribute("username", user.getUsername());
        	return "/user/apijoin";
        }
        
        return "redirect:/";
    }
    
    @GetMapping("/kakao/logout")
    public String kakaoLogout(HttpSession session) {
        String accessToken = (String) session.getAttribute("kakaoToken");

        if(accessToken != null && !"".equals(accessToken)){
            try {
            	kakaoService.kakaounlink(accessToken);
//            	kakaoService.kakaoDisconnect(accessToken);
            	
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            session.removeAttribute("kakaoToken");
            session.removeAttribute("loginMember");
        }else{
            System.out.println("accessToken is null");
        }

        return "redirect:/login/page";
    }
}
