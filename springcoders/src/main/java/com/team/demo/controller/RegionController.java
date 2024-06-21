package com.team.demo.controller;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.team.demo.domain.dto.PlaceDTO;
import com.team.demo.service.RegionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/region/*")
public class RegionController {
    //    이지호 api키
    final static String serviceKey = "dQ5KxqjeKT4EqABsyGocbGRmS1vG5PMf8OJ3MbPBVUMyor4z54ZtmdIxiXR/DxUcIKmgtj8Zn5hePQLlUIMpDw==";

//    우성문 api키
//    final static String serviceKey = "s2hxpeT8P7Kp0pQNZlnHrskliJus9MDO2Qcv1oUa1KBVwNqv5VhHdljTzYR2CjeAgBK1wPZR2g8V8s0VlJ86Tw==";

    @Autowired
    private RegionService service;

    private static final Logger log = LoggerFactory.getLogger(APIController.class);
    @GetMapping("region")
    public void region(){ }

    @PostMapping(value = "get", produces = "application/json;charset=utf-8")
    public void get(Integer contendIdValue, Integer contentTypeIdValue, Model model) throws IOException {
        System.out.println("/region/get");
//  ------------------------------------------- 1. 공통정보조회 시작----------------------------------------------

        String url = "http://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey="+serviceKey;
        url += "&MobileOS=ETC";
        url += "&MobileApp=MobileApp";


        // 응답메세지 형식 : REST방식의 URL호출 시 json값 추가(디폴트 응답메세지 형식은XML)
        String _type = "json";
        url += "&_type="+_type;

        //contentId
        int contentId = contendIdValue;
        url += "&contentId="+contentId;


        String answer = "Y";
        url += "&defaultYN="+answer;
        url += "&firstImageYN=" + answer;
        url += "&mapinfoYN=" + answer;
        url += "&overviewYN=" +answer;
        url += "&addrinfoYN=" +answer;


        url += "&numOfRows="+15;
        url += "&pageNo="+1;
        //단순한 문자열로 정의한 url 변수를 자바에서 네트워킹 때 활용할 수 있는 객체로 변환
        URL requestURL = new URL(url);
        //목적지로 향하는 다리 건설
        HttpURLConnection conn = (HttpURLConnection)requestURL.openConnection();

        conn.setRequestMethod("GET");

        //conn 다리가 건설되어 있는 목적지로부터 데이터를 읽어오기 위한 IS
        InputStream is = conn.getInputStream();
        //열려있는 IS 통로를 통해 들어오는 데이터를 읽기위한 리더기
        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader br = new BufferedReader(isr);

        String common_result = "";
        String common_line = "";
        while(true) {
            common_line = br.readLine();
            if(common_line == null) { break; }
            common_result += common_line;
        }


        Gson gson = new Gson();
        JSONObject json = new JSONObject();

        Map<String, Object> common = gson.fromJson(common_result, new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> common1 = gson.fromJson(gson.toJson(common.get("response")), new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> common2 = gson.fromJson(gson.toJson(common1.get("body")), new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> common3 = gson.fromJson(gson.toJson(common2.get("items")), new TypeToken<Map<String, Object>>(){}.getType());
        List<Map<String,String>> common_items = (List<Map<String,String>>)common3.get("item");
        Map<String, String> common_item = gson.fromJson(gson.toJson(common_items.get(0)), new TypeToken<Map<String, String>>(){}.getType());

        model.addAttribute("common", common_item);
        System.out.println("common = " + common);
//        ra.addAttribute("common", common_item);

//  ------------------------------------------- 1. 공통정보조회  끝----------------------------------------------

//  ------------------------------------------- 2. 소개정보조회 시작----------------------------------------------
        url = "http://apis.data.go.kr/B551011/KorService1/detailIntro1?serviceKey="+serviceKey;
        url += "&MobileOS=ETC";
        url += "&MobileApp=MobileApp";


        // 응답메세지 형식 : REST방식의 URL호출 시 json값 추가(디폴트 응답메세지 형식은XML)
        _type = "json";
        url += "&_type="+_type;

        //contentId
        contentId = contendIdValue;
        url += "&contentId="+contentId;

        //contenttypeid
        int contentTypeId = contentTypeIdValue;
        url += "&contentTypeId="+contentTypeId;

        url += "&numOfRows="+15;
        url += "&pageNo="+1;
        //단순한 문자열로 정의한 url 변수를 자바에서 네트워킹 때 활용할 수 있는 객체로 변환
        requestURL = new URL(url);
        //목적지로 향하는 다리 건설
        conn = (HttpURLConnection)requestURL.openConnection();

        conn.setRequestMethod("GET");

        //conn 다리가 건설되어 있는 목적지로부터 데이터를 읽어오기 위한 IS
        is = conn.getInputStream();
        //열려있는 IS 통로를 통해 들어오는 데이터를 읽기위한 리더기
        isr = new InputStreamReader(is);

        br = new BufferedReader(isr);

        String intro_result = "";
        String intro_line = "";
        while(true) {
            intro_line = br.readLine();
            if(intro_line == null) { break; }
            intro_result += intro_line;
        }

        Map<String, Object> intro = gson.fromJson(intro_result, new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> intro1 = gson.fromJson(gson.toJson(intro.get("response")), new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> intro2 = gson.fromJson(gson.toJson(intro1.get("body")), new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> intro3 = gson.fromJson(gson.toJson(intro2.get("items")), new TypeToken<Map<String, Object>>(){}.getType());
        List<Map<String,String>> intro_items = (List<Map<String,String>>)intro3.get("item");
        Map<String, String> intro_item = gson.fromJson(gson.toJson(intro_items.get(0)), new TypeToken<Map<String, String>>(){}.getType());

        model.addAttribute("intro", intro_item);
        System.out.println("intro = " + intro);



//        ra.addAttribute("intro", intro_item);

//  ------------------------------------------- 2. 소개정보조회  끝----------------------------------------------
//  ------------------------------------------- 3. 반복정보조회 시작----------------------------------------------

        // 필수 입력 요소 - 시작

        url = "http://apis.data.go.kr/B551011/KorService1/detailInfo1?serviceKey="+serviceKey;
        url += "&MobileOS=ETC";
        url += "&MobileApp=MobileApp";


        // 응답메세지 형식 : REST방식의 URL호출 시 json값 추가(디폴트 응답메세지 형식은XML)
        _type = "json";
        url += "&_type="+_type;

        //contentId
        contentId = contendIdValue;
        url += "&contentId="+contentId;

        //contenttypeid
        contentTypeId = contentTypeIdValue;
        url += "&contentTypeId="+contentTypeId;

        url += "&numOfRows="+15;
        url += "&pageNo="+1;
        //단순한 문자열로 정의한 url 변수를 자바에서 네트워킹 때 활용할 수 있는 객체로 변환
        requestURL = new URL(url);

        //목적지로 향하는 다리 건설
        conn = (HttpURLConnection)requestURL.openConnection();

        conn.setRequestMethod("GET");

        //conn 다리가 건설되어 있는 목적지로부터 데이터를 읽어오기 위한 IS
        is = conn.getInputStream();
        //열려있는 IS 통로를 통해 들어오는 데이터를 읽기위한 리더기
        isr = new InputStreamReader(is);

        br = new BufferedReader(isr);

        String info_result = "";
        String info_line = "";
        while(true) {
            info_line = br.readLine();
            if(info_line == null) { break; }
            info_result += info_line;
        }

        Map<String, Object> info = gson.fromJson(info_result, new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> info1 = gson.fromJson(gson.toJson(info.get("response")), new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> info2 = gson.fromJson(gson.toJson(info1.get("body")), new TypeToken<Map<String, Object>>(){}.getType());
//    Map<String, Object> info3 = gson.fromJson(gson.toJson(info2.get("items")), new TypeToken<Map<String, Object>>(){}.getType());

        // "items"가 비어있는지 여부를 확인하여 분기
        if (info2.get("items").toString().isEmpty()) {
            // "items"가 비어있는 경우 처리
            model.addAttribute("info", null); // 또는 원하는 기본값 설정
            System.out.println("info = " + model.getAttribute("info"));
        } else {
            // "items"에 데이터가 있는 경우 처리
            Map<String, Object> info3 = gson.fromJson(gson.toJson(info2.get("items")), new TypeToken<Map<String, Object>>(){}.getType());
            List<Map<String,String>> info_items = (List<Map<String,String>>)info3.get("item");
            Map<String, String> info_item = gson.fromJson(gson.toJson(info_items.get(0)), new TypeToken<Map<String, String>>(){}.getType());

            model.addAttribute("info", info_item);
            System.out.println("info = " + info);
        }
//

        String contendIdString = String.valueOf(contendIdValue);

        // "1000"을 앞에 붙여서 문자열을 만든 후 long 타입으로 변환
        long placeid;
        try {
            placeid = Long.parseLong("1000" + contendIdString);
        } catch (NumberFormatException e) {
            // 숫자로 변환할 수 없는 경우 예외 처리
            e.printStackTrace(); // 또는 로그에 기록
            // 예외 처리 코드 추가
            return; // 메서드 종료
        }

        // 변환한 placeid를 사용하여 다른 작업을 수행하거나 반환하거나 등의 로직 추가
        // 예시: PlaceDTO를 placeid를 사용하여 가져오는 작업
        PlaceDTO temp = service.getPlaceById(placeid);
        // temp를 사용하여 다른 작업 수행

        if(temp==null){
            service.insertPlace(common_item);

            PlaceDTO place = new PlaceDTO();
            place.setPlaceid(placeid);

            place.setAddr(common_item.get("addr1"));
            place.setPlacename(common_item.get("title"));
            if(contentTypeId == 12) {
                place.setCategory("명소");
            }else if(contentTypeId == 39){
                place.setCategory("식당");
            }
            else{
                place.setCategory("기타");
            }
            System.out.println("place = " + place);
        }

        System.out.println("contentId = " + contentId);
        System.out.println("placeid = " + placeid);
        model.addAttribute("getReply", service.getReplyList(placeid));
        System.out.println(service.getReplyList(placeid));
    }

    @GetMapping("regionAddView")
    public void regionAddView() {
        System.out.println("regionAddView");
    }

}
