package com.team.demo.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.team.demo.domain.dto.login.NaverCallback;
import com.team.demo.domain.dto.login.NaverToken;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NaverService {
	
	@Value("${naver.baseUrl}")
    private String baseUrl;
	
	@Value("${naver.client.id}")
    private String clientId;
	
	@Value("${naver.secret}")
    private String naverSecret;
	
	public String getNaverTokenUrl(String type, NaverCallback callback) throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {

        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(baseUrl + "/" + type)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("client_secret", naverSecret)
                .queryParam("code", callback.getCode())
                .queryParam("state", URLEncoder.encode(callback.getState(), StandardCharsets.UTF_8))
                .build();

        try {
            URL url = new URL(uriComponents.toString());
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
	
	public String getNaverUserByToken(NaverToken token) {

        try {
            String accessToken = token.getAccess_token();
            String tokenType = token.getToken_type();

            URL url = new URL("https://openapi.naver.com/v1/nid/me");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", tokenType + " " + accessToken);

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	public void naverunlink(String type, String accessToken) throws IOException {
		try {
			UriComponents uriComponents = UriComponentsBuilder
			        .fromUriString(baseUrl + "/" + type)
			        .queryParam("grant_type", "delete")
			        .queryParam("client_id", clientId)
			        .queryParam("client_secret", naverSecret)
			        .queryParam("access_token", accessToken)
			        .build();
			URL url = new URL(uriComponents.toString());
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");

			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String line = "";
			String result = "";

			while ((line = br.readLine()) != null) {
				result += line;
			}
			System.out.println(result);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
