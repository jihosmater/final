package com.team.demo.controller;

import com.team.demo.domain.dto.ReplyDTO;
import com.team.demo.domain.dto.UserDTO;
import com.team.demo.service.ReplyService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/reply/*")
@RestController
public class ReplyController {

    @Autowired
    private ReplyService service;

    //ResponseEntity : 서버의 상태코드, 응답 메세지, 응답 데이터 등을 담을 수 있는 타입
    //consumes : 이 메소드가 호출될 때 소비할 데이터의 타입(넘겨지는 RequestBody의 데이터 타입)
    //@RequestBody : 넘겨지는 body의 내용을 해석해서 해당 파라미터에 채워넣기
    //produces : 이 메소드가 호출될 때 생성할 데이터의 타입(돌려주는 ResponseBody의 데이터 타입)
    @PostMapping(value="regist", consumes = "application/json", produces = "application/json;charset=utf-8")
    public ResponseEntity<ReplyDTO> regist(@RequestBody ReplyDTO reply, @RequestParam("placeid") String placeid, HttpServletRequest req){
        System.out.println("placeid = " + placeid);

        HttpSession session = req.getSession();
        String loginUser = session.getAttribute("loginUser").toString();

        System.out.println("loginUser = " + loginUser);
        reply.setUserid(loginUser);
        System.out.println("reply = " + reply);

        long placeId = Long.parseLong("1000"+placeid);
        int already = service.getAlready(placeId,reply.getUserid());

        ReplyDTO result;
        if(already == 1){
            result = null;
        }
        else{
        result = service.regist(reply,placeId);
        }

        if(result == null) {
            return new ResponseEntity<ReplyDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else {
            return new ResponseEntity<ReplyDTO>(result,HttpStatus.OK);
        }
    }

    @GetMapping(value = "list/{replynum}/{contentid}")
    public ResponseEntity<List<ReplyDTO>> getList(@PathVariable("contentid") long contentid){
        System.out.println("contentid = " + contentid);

        contentid = Long.parseLong("1000" + contentid);
        List<ReplyDTO> replyList = service.getAllList(contentid);

        System.out.println("replyList = " + replyList);
        return new ResponseEntity<>(replyList, HttpStatus.OK);
    }

    @DeleteMapping(value="{replynum}")
    public ResponseEntity<String> remove(@PathVariable("replynum") long replynum){
        System.out.println("replynum = " + replynum);
        return service.remove(replynum) ?
                new ResponseEntity<String>(HttpStatus.OK) :
                new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
