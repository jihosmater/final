package com.team.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.demo.domain.dto.login.KakaoInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KakaoService {

	public String getAccessTokenFromKakao(String client_id, String code) throws IOException {
		// ------kakao POST 요청------
		String reqURL = "https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=" + client_id
				+ "&code=" + code;
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		String line = "";
		String result = "";

		while ((line = br.readLine()) != null) {
			result += line;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
		});

		log.info("Response Body : " + result);

		String accessToken = (String) jsonMap.get("access_token");
		String refreshToken = (String) jsonMap.get("refresh_token");
		String scope = (String) jsonMap.get("scope");

		log.info("Access Token : " + accessToken);
		log.info("Refresh Token : " + refreshToken);
		log.info("Scope : " + scope);

		return accessToken;
	}

	public KakaoInfo getUserInfo(String access_Token) throws IOException {
		// 클라이언트 요청 정보
		HashMap<String, Object> userInfo = new HashMap<String, Object>();

		// ------kakao GET 요청------
		String reqURL = "https://kapi.kakao.com/v2/user/me";
		URL url = new URL(reqURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Authorization", "Bearer " + access_Token);

		int responseCode = conn.getResponseCode();
		System.out.println("responseCode : " + responseCode);

		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		String line = "";
		String result = "";

		while ((line = br.readLine()) != null) {
			result += line;
		}

		log.info("Response Body : " + result);

		// jackson objectmapper 객체 생성
		ObjectMapper objectMapper = new ObjectMapper();
		// JSON String -> Map
		Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
		});

		// 사용자 정보 추출
		Map<String, Object> properties = (Map<String, Object>) jsonMap.get("properties");
		Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");

		Long id = (Long) jsonMap.get("id");
		String nickname = properties.get("nickname").toString();
		String profileImage = properties.get("profile_image").toString();

		return new KakaoInfo(id, nickname, profileImage);
	}

	public void kakaoDisconnect(String accessToken) throws JsonProcessingException {
		String reqURL = "https://kauth.kakao.com/oauth/logout";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);

			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String result = "";
			String line = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void kakaounlink(String accessToken) {
		String reqURL = "https://kapi.kakao.com/v1/user/unlink";
		try {
			URL url = new URL(reqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);

			int responseCode = conn.getResponseCode();
			System.out.println("responseCode : " + responseCode);

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String result = "";
			String line = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}