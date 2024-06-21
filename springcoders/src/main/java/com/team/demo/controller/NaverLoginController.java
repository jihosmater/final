package com.team.demo.controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.demo.domain.dto.UserDTO;
import com.team.demo.domain.dto.login.NaverCallback;
import com.team.demo.domain.dto.login.NaverRes;
import com.team.demo.domain.dto.login.NaverToken;
import com.team.demo.service.NaverService;
import com.team.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NaverLoginController {

	@Value("${naver.baseUrl}")
    private String baseUrl;
	
	@Value("${naver.client.id}")
    private String clientId;
	
	@Value("${naver.redirect.url}")
    private String redirectUrl;
	
	@Autowired
	private NaverService naverService;
	
	@Autowired
	private UserService userService;
	
	String type = "authorize";
	
	@GetMapping("/naverlogin/getNaverAuthUrl")
    public void naverLogin(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException {
		UriComponents uriComponents = UriComponentsBuilder
	            .fromUriString(baseUrl + "/" + type)
	            .queryParam("response_type", "code")
	            .queryParam("client_id", clientId)
	            .queryParam("redirect_uri", URLEncoder.encode(redirectUrl, "UTF-8"))
	            .queryParam("state", URLEncoder.encode("1234", "UTF-8"))
	            .build();

	    String url =  uriComponents.toString();
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	//callback해서 받아올 NaverCallback 클래스 생성(dto?)
	//callback에서 응답받는 모든 값을 넣어야함(*중요)
	@GetMapping("/naver/callback")
    public String callBack(HttpServletRequest request, HttpServletResponse response, NaverCallback callback, HttpSession session, Model model) throws MalformedURLException, UnsupportedEncodingException, URISyntaxException, JsonProcessingException {

        if (callback.getError() != null) {
            System.out.println(callback.getError_description());
        }
        
        //accessToken 발급
        String responseToken = naverService.getNaverTokenUrl("token", callback);
        
        //json을 java Object로 변환
        ObjectMapper mapper = new ObjectMapper();
        //json으로 되어있는 문자열 responseToken을 dto에 넣음
        NaverToken token = mapper.readValue(responseToken, NaverToken.class);
        //유저 정보 받아옴
        String responseUser = naverService.getNaverUserByToken(token);
        //json으로 되어있는 문자열 responseUser을 dto에 넣음
        //responseUser에 있는 호출 결과를 제외한 유저 정보들은 response란 이름의 json안에 있음
        //dto만들때도 NaverRes(dto)안에 response(dto)를 호출해서 사용하는 식으로 해야함
        NaverRes naverUser = mapper.readValue(responseUser, NaverRes.class);

        System.out.println("naverUser.toString() : " + naverUser.toString());
        System.out.println("naverUser.getResonse().getId() : " + naverUser.getResponse().getId());
        System.out.println("naverUser.getResonse().getName() : " + naverUser.getResponse().getName());
        
        String userid = "NAVER"+naverUser.getResponse().getId();
        String username = naverUser.getResponse().getName();
        String userpw = userService.randpass();
    	
    	if(!(userService.checkId(userid)==null)) {
    		UserDTO user = userService.checkId(userid);
        	session.setAttribute("loginUser", userid	);
            // session.setMaxInactiveInterval( ) : 세션 타임아웃을 설정하는 메서드
            // 로그인 유지 시간 설정 (3600초 == 60분 60분 * 6 총 6시간)
            session.setMaxInactiveInterval(60 * 60 * 6);
            
            session.setAttribute("naverToken", token.getAccess_token());
            session.setAttribute("loginName", username);
            System.out.println(token.getAccess_token());
        }
        else {
        	UserDTO user = new UserDTO(userid, userpw, username);
        	System.out.println(user);
        	model.addAttribute("userid", user.getUserid());
        	model.addAttribute("userpw", user.getUserpw());
        	model.addAttribute("username", user.getUsername());
        	
        	return "/user/apijoin";
        }
    	return "redirect:/";
    }
	
	@GetMapping("/naver/logout")
    public String naverLogout(HttpSession session) {
        String accessToken = (String) session.getAttribute("naverToken");

        if(accessToken != null && !"".equals(accessToken)){
            try {
            	naverService.naverunlink("token", accessToken);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            session.removeAttribute("naverToken");
            session.removeAttribute("loginMember");
        }else{
            System.out.println("accessToken is null");
        }

        return "redirect:/user/login2";
    }
	
}
