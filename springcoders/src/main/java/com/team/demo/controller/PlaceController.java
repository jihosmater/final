package com.team.demo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.team.demo.domain.dto.Criteria;
import com.team.demo.domain.dto.PageDTO;
import com.team.demo.domain.dto.PlaceDTO;
import com.team.demo.service.PlaceService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/place/*")
public class PlaceController {
    @Autowired
    private PlaceService service;

    // ResponseEntity : 서버의 상태코드, 응답 메세지, 응답 데이터 등을 담을 수 있는 타입
    // consumes : 이 메소드가 호출될 때 소비할 데이터의 타입(넘겨지는 RequestBody의 데이터 타입)
    // produces : 이 메소드가 호출될 때 생성할 데이터의 타입(돌려주는 ResponseBody의 데이터 타입)
    // @ResponseBody: 컨트롤러 메서드가 반환하는 값을 HTTP 응답 본문으로 변환합니다.
    // @RequestBody : 넘겨지는 body의 내용을 해석해서 해당 파라미터에 채워 넣기
    // @RequestBody: HTTP 요청 본문을 자바 객체로 변환하여 컨트롤러 메서드의 매개변수로 전달합니다.
    @PostMapping(value="add", produces = "application/json;charset=utf-8")
    public ResponseEntity<String> AddPlace(PlaceDTO place, boolean order,String categoryTag, HttpServletRequest req) {
        System.out.println("/place/add : POST");
        Object obj = req.getSession().getAttribute("loginUser");
        String loginUser = (String)obj;
        if(loginUser.equals("")){
            return new ResponseEntity<String>("notLogin", HttpStatus.OK);
        }
        PlaceDTO temp = service.getDetail(place.getPlaceid());
        place.setCategory(categoryTag+"/&&/"+place.getCategory());
        if(temp == null){
            if(service.insertPlace(place)){
                service.getImgByCrawling(place);
                if(order){
                    service.addFavoriteList(loginUser,place.getPlaceid());
                    return new ResponseEntity<String>("OAF", HttpStatus.OK);
                }
                return new ResponseEntity<String>("O", HttpStatus.OK);
            }
            return new ResponseEntity<String>("X", HttpStatus.OK);
        }else{
            if(order){
                if(service.addFavoriteList(loginUser,place.getPlaceid())){
                    //AF = addFavorite : 즐겨찾기 저장
                    return new ResponseEntity<String>("AF", HttpStatus.OK);
                }
                return new ResponseEntity<String>("EAF", HttpStatus.OK);
            }
            //E = exist : 이미 저장된 장소
            return new ResponseEntity<String>("E", HttpStatus.OK);
        }
    }

    @PostMapping(value = "getList" ,produces = "application/json;charset=utf-8")
    public ResponseEntity<String> GetList(Criteria cri, Model model,HttpServletRequest req){
        System.out.println("/place/getList : POST");
//        System.out.println(cri);
//        String loginUser = (req.getSession().getAttribute("loginUser")==null?"":(String)req.getSession().getAttribute("loginUser"));
        String loginUser = "apple";
        if(cri.getType().equals("좋아요")){
            if(loginUser.equals("")){
                return new ResponseEntity<String>("notLogin",HttpStatus.OK);
            }
        }

        Gson gson = new Gson();
        JsonObject json = new JsonObject();

        List<PlaceDTO> list = service.getList(cri,loginUser);
        // place 리스트
        json.add("placeList",gson.toJsonTree(list));
        // place 이미지 리스트
        json.add("imgList",gson.toJsonTree(service.getPlcaeimgList(list)));
        // place likeCnt
        json.add("likeCntList",gson.toJsonTree(service.getLikeCntList(cri,loginUser)));
        // Criteria
        json.add("pageMaker",gson.toJsonTree(new PageDTO(service.getTotal(cri,loginUser), cri,5)));

        String map = gson.toJson(json);
//        System.out.println(map);
        return new ResponseEntity<String>(map,HttpStatus.OK);
    }


}


