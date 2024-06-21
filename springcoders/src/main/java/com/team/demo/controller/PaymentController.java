package com.team.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.team.demo.domain.dto.UserDTO;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/payment/*")
public class PaymentController {
   
   @GetMapping("KGpayment")
   public String KGpayment() {
      return "payment/KGpayment";
   }
   
   @PostMapping("KGpayment")
   public void KGpayment(UserDTO userid) {
      System.out.println("포트원 KG이니시스 결제API 실행!");
   }




}