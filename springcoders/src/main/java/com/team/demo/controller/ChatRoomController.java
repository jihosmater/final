package com.team.demo.controller;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.team.demo.domain.dto.ChatMessageDTO;
import com.team.demo.domain.dto.ChatRoomDTO;
import com.team.demo.domain.dto.ChatUserDTO;
import com.team.demo.domain.dto.UserDTO;
import com.team.demo.service.ChatService;
import com.team.demo.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    
    @Autowired
    private ChatService service;
    
    @Autowired 
    private UserService userservice;
    
    
    @GetMapping(value="/chatButton")
    public void chatButton(){
    	
    }
    
    
    //채팅방 목록 조회
    @GetMapping(value = "/rooms")
    public ResponseEntity<List<List<Object>>> rooms(HttpSession session, Model model){
    	String loginMember = (String) session.getAttribute("loginUser");

    	List<ChatUserDTO> list = service.getAllUserRoomByUserId(loginMember);
    	List<List<Object>> chatRoomInfos = new ArrayList<>();
    	for(int i = 0; i < list.size(); i++) {
    		List<Object> chatRoomInfo = new ArrayList<>();
    		ChatUserDTO chatuser = list.get(i);
    		List<UserDTO> user = userservice.getUserContainChatUser(chatuser.getRoomno());
    		ChatMessageDTO message = service.lastMessage(chatuser.getRoomno());	
    		int unseenMessage = service.unseenMessage(loginMember, chatuser.getRoomno());
    		ChatRoomDTO chatroom = service.chatRoomGet(chatuser.getRoomno());
    		chatRoomInfo.add(chatuser);
    		chatRoomInfo.add(user);
    		chatRoomInfo.add(message);
    		chatRoomInfo.add(unseenMessage);
    		chatRoomInfo.add(chatroom);
    		chatRoomInfos.add(chatRoomInfo);
    	}
    	return ResponseEntity.ok().body(chatRoomInfos);
    }

    //채팅방 개설
    @PostMapping(value = "/room", produces = "application/json;charset=utf-8")
    public ResponseEntity<String> create(@RequestParam String adminid, @RequestParam String roomname, HttpServletRequest req){
    	HttpSession session = req.getSession();
		String loginUser = (String)session.getAttribute("loginUser");
		System.out.println("야호홍호오홍호홍호오홍"+roomname);
		int duplicate = service.countDuplicateUsers(roomname, adminid, loginUser);
		if(duplicate == 0) {
			service.chatRoomInsert(roomname, loginUser);
			int roomno = service.lastInsertRoomId();
			service.chatUserInsert(roomno, roomname, loginUser);
			if(service.chatUserInsert(roomno, roomname, adminid)) {				
				return new ResponseEntity<String>("O", HttpStatus.OK);
			}
		}
		else if(duplicate <= 3){
			return new ResponseEntity<String>("X", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("N", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //채팅방 조회
    @GetMapping("/room")
    public String getRoom(@RequestParam(value = "roomno") int roomno, HttpSession session, Model model){
    	String loginMember = (String) session.getAttribute("loginUser");
    	model.addAttribute("chatroom", service.chatRoomGet(roomno));
    	model.addAttribute("loginMember", loginMember);
    	return "chat/room";
    }
    
    @PostMapping(value="delete/{roomno}")
	public ResponseEntity<String> delete(@PathVariable("roomno")int roomno, HttpSession session){
		String loginMember = (String) session.getAttribute("loginUser");
		System.out.println("delete 들어옴");
		
		System.out.println(roomno);
		ChatMessageDTO chatMessage = new ChatMessageDTO();
		chatMessage.setRoomno(roomno);
		chatMessage.setUserid(loginMember);
		chatMessage.setType("MESSAGE");
		chatMessage.setMessage(userservice.getUserinfo(loginMember).getUsername()+"님이 퇴장하셨습니다.");
    	System.out.println(chatMessage);
		service.chatMessageInsert(chatMessage);
		if(service.chatUserDelete(roomno, loginMember)) {			
			System.out.println("성공");
			return new ResponseEntity<String>(HttpStatus.OK);
		}
		System.out.println("실패");
		return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}