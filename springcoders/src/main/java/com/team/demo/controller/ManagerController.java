package com.team.demo.controller;

import com.team.demo.domain.dto.BoardDTO;
import com.team.demo.domain.dto.Criteria;
import com.team.demo.domain.dto.PageDTO;
import com.team.demo.service.ManagerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/manager/*")
public class ManagerController {

    @Autowired
    ManagerService service;

    @GetMapping("agree")
    public void agree(Model model, HttpServletRequest req) {
        Object temp = req.getSession().getAttribute("loginUser");
        String manager = "";
        if (temp != null) {
            manager = (String) temp;
        } else {
            System.out.println(" 로그인 안되어잇음");
        }

        Map<String, Integer> result = service.manageInfo();

        // 관리 정보를 모델에 추가
        model.addAttribute("boardCnt", result.get("boardCnt"));
        model.addAttribute("userCnt", result.get("userCnt"));
        model.addAttribute("completedCnt", result.get("completedCnt"));
        model.addAttribute("requestCnt", result.get("requestCnt"));

        model.addAttribute("manager", manager);
    }

//    @PostMapping("accept")
//    public void accept(Criteria cri, Model model) {
//        System.out.println("cri = " + cri);
//        cri.setAmount(10);
//        List<BoardDTO> list = service.notAllowedBoardInfo(cri);
//        System.out.println("list = " + list);
//        model.addAttribute("list", list);
//        model.addAttribute("pageMaker", new PageDTO(service.getTotal(cri),cri, 10));
//    }

    @PostMapping("accept")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> accept(Criteria cri) {
        System.out.println("cri = " + cri);
        cri.setAmount(10);
        List<Map<String, Object>> list = service.notAllowedBoardInfo(cri);
        System.out.println("list = " + list);

        Map<String, Object> response = new HashMap<>();
        response.put("list", list);
        response.put("pageMaker", new PageDTO(service.getTotal(cri), cri, 10));

        System.out.println("response = " + response);
        return ResponseEntity.ok(response);
    }
//    @PostMapping(value = "/like", produces = "application/json;charset=utf-8")
//    public ResponseEntity<String> likePlace(@RequestParam long placeid, HttpServletRequest req) {
//        placeid = Long.parseLong("1000" + placeid);
//        HttpSession session = req.getSession();
//        String loginUser = (String)session.getAttribute("loginUser");
//        if (loginUser == null) {
//            return new ResponseEntity<String>("X", HttpStatus.OK);
//        } else {
//            if(service.likePlace(loginUser, placeid)) {
//                return new ResponseEntity<String>("O", HttpStatus.OK);
//            } else {
//                return new ResponseEntity<String>("Y", HttpStatus.OK);
//            }
//        }
//    }

    @PostMapping(value = "complete", produces = "application/json;charset=utf-8")
    public ResponseEntity<String> complete(@RequestParam long boardnum) {

        if(service.completeDeal(boardnum)){
            return new ResponseEntity<>("O", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("X", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "cancel", produces = "application/json;charset=utf-8")
    public ResponseEntity<String> cancel(@RequestParam long boardnum) {

        if(service.cancelDeal(boardnum)){
            return new ResponseEntity<>("O", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("X", HttpStatus.BAD_REQUEST);
        }
    }

}
