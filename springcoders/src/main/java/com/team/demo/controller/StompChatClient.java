package com.team.demo.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.team.demo.domain.dto.ChatMessageDTO;
import com.team.demo.domain.dto.ChatRoomDTO;
import com.team.demo.domain.dto.UserDTO;
import com.team.demo.service.ChatService;
import com.team.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class StompChatClient {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ChatService service;
    
    @Autowired
    private UserService userservice;
    
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessageDTO chatMessage) {
    	String loginMember = chatMessage.getUserid();
        //chatMessage.getUserid()
        if(service.CheckUser(chatMessage.getRoomno(), loginMember)) {
        	chatMessage.setType("RE_ENTER");
        	System.out.println(chatMessage.getType());
        	messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomno(), chatMessage);
        	
        }else {
        	System.out.println(chatMessage.getType());
        	ChatRoomDTO chatroom = service.chatRoomGet(chatMessage.getRoomno());
        	UserDTO user = userservice.getUserinfo(loginMember);
        	chatMessage.setMessage(user.getUsername()+"님이 입장하셨습니다.");
//        	service.chatUserInsert(chatMessage.getRoomno(), chatroom.getRoomname(), loginMember);
        	service.chatMessageInsert(chatMessage);
        	messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomno(), chatMessage);
        }
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessageDTO chatMessage) {
    	if(service.chatMessageInsert(chatMessage)){
    		chatMessage = service.lastMessage(chatMessage.getRoomno());
        	messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomno(), chatMessage);
        }
        else{
        	chatMessage.setType("ERROR");
        	messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomno(), chatMessage);
        }
    }
    
    @MessageMapping(value = "/chat/image")
    public void image(ChatMessageDTO chatMessage) {
    	if(service.chatMessageInsert(chatMessage)){
    		chatMessage = service.lastImage(chatMessage.getRoomno());
        	messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomno(), chatMessage);
        }
        else{
        	chatMessage.setType("ERROR");
        	messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomno(), chatMessage);
        }
    }
    
    @MessageMapping(value = "/chat/imoticon")
    public void imoticon(ChatMessageDTO chatMessage) {
    	if(service.chatMessageInsert(chatMessage)){
    		chatMessage = service.lastImoticon(chatMessage.getRoomno());
        	messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomno(), chatMessage);
        }
        else{
        	chatMessage.setType("ERROR");
        	messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomno(), chatMessage);
        }
    }
}