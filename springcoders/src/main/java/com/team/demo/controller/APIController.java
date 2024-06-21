package com.team.demo.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/*")
public class APIController {
//    이지호 api키
    final static String serviceKey = "dQ5KxqjeKT4EqABsyGocbGRmS1vG5PMf8OJ3MbPBVUMyor4z54ZtmdIxiXR/DxUcIKmgtj8Zn5hePQLlUIMpDw==";

//    우성문 api키
//    final static String serviceKey = "s2hxpeT8P7Kp0pQNZlnHrskliJus9MDO2Qcv1oUa1KBVwNqv5VhHdljTzYR2CjeAgBK1wPZR2g8V8s0VlJ86Tw==";

    final static String serviceKey2 = "UUDB0vs6itcTbwApIeUGBU9sx/gLiwNDA9pNA7vcOR9CHnE0v3MOuWLEvlPBoNzaWvvADIaX6iFljR2N9UixMQ==";
    private static final Logger log = LoggerFactory.getLogger(APIController.class);

    @GetMapping("get")
    public void replace() {}
    //집에가고싶다

    @PostMapping(value = "get", produces = "application/json;charset=utf-8")
    @ResponseBody
    public ArrayList<String> getData(String arrange, int areaCode, String order) throws Exception {
        // 필수 입력 요소 - 시작
        ArrayList<String> returnStr = new ArrayList<>();

        //1. 지역행사 데이터 요청 시작
        String url = "http://apis.data.go.kr/B551011/KorService1/searchFestival1?serviceKey="+serviceKey;
        url += "&MobileOS=ETC";
        url += "&MobileApp=MobileApp";

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue();

        // 시작일
        String eventStartDate = String.format("%04d%02d01", year, month);

        // 종료일
        YearMonth yearMonth = YearMonth.of(year, month);
        int lastDayOfMonth = yearMonth.lengthOfMonth();
        String eventEndDate = String.format("%04d%02d%02d", year, month, lastDayOfMonth);

        // 윤년 계산
        boolean isLeapYear = year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
        if (isLeapYear && month == 2) {
            eventEndDate = String.format("%04d%02d%02d", year, month, 29);
        }

        System.out.println("현재 연도: " + year);
        System.out.println("현재 월: " + month);
        System.out.println("시작일: " + eventStartDate);
        System.out.println("종료일: " + eventEndDate);

        url += "&eventStartDate="+eventStartDate ;
        url += "&eventEndDate="+eventEndDate ;

        // 응답메세지 형식 : REST방식의 URL호출 시 json값 추가(디폴트 응답메세지 형식은XML)
        String _type = "json";
        url += "&_type="+_type;

        // 페이지 번호
        int pageNo = 1;
        url += "&pageNo="+pageNo;
        if(!order.equals("area")){
            // 한페이지 결과수
            int numOfRows = 10;
            url += "&numOfRows="+numOfRows;
        }
        else if(areaCode!=0) {
            // 지역 코드 번호
            url += "&areaCode="+areaCode;

            int numOfRows = 15;
            url += "&numOfRows="+numOfRows;
        }
        else {
            int numOfRows = 15;
            url += "&numOfRows="+numOfRows;
        }

        //정렬 기준 정렬구분 (A=제목순, C=수정일순, D=생성일순) 대표이미지가반드시있는정렬(O=제목순, Q=수정일순, R=생성일순)
        HashMap<String,String> arrangeList = new HashMap<>();
        arrangeList.put("제목순","A");
        arrangeList.put("수정일순","C");
        arrangeList.put("생성일순","D");
        arrangeList.put("대표이미지포함-제목순","O");
        arrangeList.put("대표이미지포함-수정일순","Q");
        arrangeList.put("대표이미지포함-생성일순","R");
        url += "&arrange="+arrangeList.get(arrange);

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

        String result = "";
        String line = "";
        while(true) {
            line = br.readLine();
            if(line == null) { break; }
            result += line;
        }
        returnStr.add(result);
//        1. 지역행사 데이터 요청 끝 ------------------------------------------------------


//        2. 숙박 데이터 요청 시작---------------------------------------------------------
        url = "https://apis.data.go.kr/B551011/KorService1/searchStay1?serviceKey="+serviceKey;
        url += "&MobileOS=ETC";
        url += "&MobileApp=MobileApp";


        // 응답메세지 형식 : REST방식의 URL호출 시 json값 추가(디폴트 응답메세지 형식은XML)
        _type = "json";
        url += "&_type="+_type;

        // 페이지 번호
        pageNo = 1;
        url += "&pageNo="+pageNo;
        if(!order.equals("area")){
            // 한페이지 결과수
            int numOfRows = 10;
            url += "&numOfRows="+numOfRows;
        }
        else if(areaCode!=0) {
            // 지역 코드 번호
            url += "&areaCode="+areaCode;

            int numOfRows = 15;
            url += "&numOfRows="+numOfRows;
        }
        else {
            int numOfRows = 15;
            url += "&numOfRows="+numOfRows;
        }


        arrangeList = new HashMap<>();
        arrangeList.put("제목순","A");
        arrangeList.put("수정일순","C");
        arrangeList.put("생성일순","D");
        arrangeList.put("대표이미지포함-제목순","O");
        arrangeList.put("대표이미지포함-수정일순","Q");
        arrangeList.put("대표이미지포함-생성일순","R");
        url += "&arrange="+"O";



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

        String hot_result = "";
        String hot_line = "";
        while(true) {
            hot_line = br.readLine();
            if(hot_line == null) { break; }
            hot_result += hot_line;
        }
        returnStr.add(hot_result);

//        2. 숙박 데이터 요청 끝---------------------------------------------------------

//        3. 여행코스 데이터 요청 시작---------------------------------------------
        url = "http://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey="+serviceKey;
        url += "&MobileOS=ETC";
        url += "&MobileApp=MobileApp";



        // 응답메세지 형식 : REST방식의 URL호출 시 json값 추가(디폴트 응답메세지 형식은XML)
        _type = "json";
        url += "&_type="+_type;

        // 페이지 번호
        pageNo = 1;
        url += "&pageNo="+pageNo;
        if(!order.equals("area")){
            // 한페이지 결과수
            int numOfRows = 10;
            url += "&numOfRows="+numOfRows;
        }
        else if(areaCode!=0) {
            // 지역 코드 번호
            url += "&areaCode="+areaCode;

            int numOfRows = 15;
            url += "&numOfRows="+numOfRows;
        }
        else {
            int numOfRows = 15;
            url += "&numOfRows="+numOfRows;
        }


        int contentTypeId = 15;
        url += "&contentTypeId="+contentTypeId;




        url += "&arrange="+"O";

        System.out.println(url);
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

        String Travel_course = "";
        String course_line = "";
        while(true) {
            course_line = br.readLine();
            if(course_line == null) { break; }
            Travel_course += course_line;
        }
        returnStr.add(Travel_course);



//       3. 여행코스 데이터 요청 끝 ------------------------------------------------------


//       4. 관광지 데이터 요청 시작 ------------------------------------------------------
        url = "http://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey="+serviceKey;
        url += "&MobileOS=ETC";
        url += "&MobileApp=MobileApp";



        // 응답메세지 형식 : REST방식의 URL호출 시 json값 추가(디폴트 응답메세지 형식은XML)
        _type = "json";
        url += "&_type="+_type;

        // 페이지 번호
        pageNo = 1;
        url += "&pageNo="+pageNo;
        if(!order.equals("area")){
            // 한페이지 결과수
            int numOfRows = 10;
            url += "&numOfRows="+numOfRows;
        }
        else if(areaCode!=0) {
            // 지역 코드 번호
            url += "&areaCode="+areaCode;

            int numOfRows = 15;
            url += "&numOfRows="+numOfRows;
        }
        else {
            int numOfRows = 15;
            url += "&numOfRows="+numOfRows;
        }


        contentTypeId = 12;
        url += "&contentTypeId="+contentTypeId;



        url += "&arrange="+"O";

        System.out.println(url);
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

        String recommend_spot = "";
        String spot_line = "";
        while(true) {
            spot_line = br.readLine();
            if(spot_line == null) { break; }
            recommend_spot += spot_line;
        }
        returnStr.add(recommend_spot);

        System.out.println(recommend_spot);
//       4. 관광지 데이터 요청 끝 ------------------------------------------------------

//       5. 맛집 데이터 요청 시작 ------------------------------------------------------
        url = "http://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey="+serviceKey;
        url += "&MobileOS=ETC";
        url += "&MobileApp=MobileApp";



        // 응답메세지 형식 : REST방식의 URL호출 시 json값 추가(디폴트 응답메세지 형식은XML)
        _type = "json";
        url += "&_type="+_type;

        // 페이지 번호
        pageNo = 1;
        url += "&pageNo="+pageNo;
        if(!order.equals("area")){
            // 한페이지 결과수
            int numOfRows = 10;
            url += "&numOfRows="+numOfRows;
        }
        else if(areaCode!=0) {
            // 지역 코드 번호
            url += "&areaCode="+areaCode;

            int numOfRows = 15;
            url += "&numOfRows="+numOfRows;
        }
        else {
            int numOfRows = 15;
            url += "&numOfRows="+numOfRows;
        }


        contentTypeId = 39;
        url += "&contentTypeId="+contentTypeId;

        url += "&arrange="+"O";

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

        String recommend_food = "";
        String food_line = "";
        while(true) {
            food_line = br.readLine();
            if(food_line == null) { break; }
            recommend_food += food_line;
        }
        returnStr.add(recommend_food);


//       5. 맛집 데이터 요청 끝 ------------------------------------------------------

        return returnStr;
    }

    @PostMapping(value = "move", produces = "application/json;charset=utf-8")
    public String move(Integer contendIdValue, Integer contentTypeIdValue, Model model, RedirectAttributes ra) throws Exception {
        System.out.println("/api/move Post방식");
        ArrayList<String> returnStr = new ArrayList<>();

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

        url += "&numOfRows="+100;
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

//  ------------------------------------------- 3. 반복정보조회  끝----------------------------------------------
//  ------------------------------------------- 4. 키워드정보 시작----------------------------------------------
        url = "https://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey="+serviceKey;
        url += "&MobileOS=ETC";
        url += "&MobileApp=MobileApp";


        // 응답메세지 형식 : REST방식의 URL호출 시 json값 추가(디폴트 응답메세지 형식은XML)
        _type = "json";
        url += "&_type="+_type;


        String _keyword = "서울";
        url += "&keyword="+_keyword;
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

        String keyword_result = "";
        String keyword_line = "";
        while(true) {
            keyword_line = br.readLine();
            if(keyword_line == null) { break; }
            keyword_result += keyword_line;
        }

        Map<String, Object> keyword = gson.fromJson(keyword_result, new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> keyword1 = gson.fromJson(gson.toJson(keyword.get("response")), new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> keyword2 = gson.fromJson(gson.toJson(keyword1.get("body")), new TypeToken<Map<String, Object>>(){}.getType());
        Map<String, Object> keyword3 = gson.fromJson(gson.toJson(keyword2.get("items")), new TypeToken<Map<String, Object>>(){}.getType());
        List<Map<String,String>> keyword_items = (List<Map<String,String>>)keyword3.get("item");
        Map<String, String> keyword_item = gson.fromJson(gson.toJson(keyword_items.get(0)), new TypeToken<Map<String, String>>(){}.getType());

        model.addAttribute("keyword", keyword_item);
        System.out.println("keyword = " + keyword);
//  ------------------------------------------- 4. 키워드정보 끝----------------------------------------------


//

        System.out.println("test = api 컨트롤러 move 테스트 시작 " );
        return "/region/get";
    }
    @PostMapping(value = "regionAddView", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getDetailData(String keyword, int numOfRows, int pageNo, int areacode) throws Exception {

        // 1. 공공데이터 요청 시작
        // 공공데이터 포탈 마이페이지 개인코드
        String url = "https://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey="+serviceKey2;

        System.out.println("키워드 파라미터 : "+keyword);
        System.out.println("지역번호 파라미터 : "+areacode);

        // 매개변수로 받은 keyword 인코딩
        String encodedkeyword = URLEncoder.encode(keyword, "UTF-8");

        System.out.println("_keyword="+encodedkeyword);

        url += "&keyword="+encodedkeyword;
        // 2. 파라미터 보내기
        url += "&MobileOS=ETC";
        url += "&MobileApp=suggest";
        url += "&arrange=O";
        url += "&contentTypeId=12";
        url +="&areaCode="+areacode;

        // 응답메세지 형식 : REST방식의 URL호출시 json값 추가(디폴트 응답메세지 형식은XML)
        String _type = "json";
        url +="&_type="+_type;

        // 페이지 번호와 한페이지 결과 수
//          int numOfRows = 8;
//          int pageNo = 1;

        url += "&numOfRows="+numOfRows;
        url += "&pageNo="+pageNo;

        System.out.println("파라미터 확인 : "+url);

        // 3. 단순한 문자열로 정의한 url 변수를 자바에서 네트워킹 때 활용할 수 있는 객체로 변환
        URL requestURL = new URL(url);
        // 목적지로 향하는 다리건설
        HttpURLConnection conn =
                (HttpURLConnection)requestURL.openConnection();

        conn.setRequestMethod("GET");

        // conn 다리가 건설되어 있는 목적지로부터 데이터를 읽어오기 위한 is
        InputStream is = conn.getInputStream();
        // 열려있는 is 통로를 통해 들어오는 데이터를 읽기위한 리더기
        InputStreamReader isr = new InputStreamReader(is);

        BufferedReader br = new BufferedReader(isr);

        String result = "";
        String line = "";
        while(true) {

            line = br.readLine();

            if(line == null) {
                break;
            }
            result += line;
        }


        System.out.println("컨트롤러 리턴값 확인 : "+result);

        return result;
    }

}
