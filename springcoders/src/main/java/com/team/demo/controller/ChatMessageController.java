package com.team.demo.controller;


import com.team.demo.domain.dto.ChatMessageDTO;
import com.team.demo.domain.dto.ChatUserDTO;
import com.team.demo.domain.dto.UserDTO;
import com.team.demo.service.ChatService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chatting/*")
public class ChatMessageController {
	@Autowired
    private ChatService service;
	
	@GetMapping(value = "ready/{roomno}")
	public ResponseEntity<List<ChatMessageDTO>> getList(@PathVariable("roomno")int roomno, HttpSession session){
		String loginMember = (String) session.getAttribute("loginUser");
		
		List<ChatMessageDTO> chatMessageList = service.chatMessageList(roomno);
		int last_read_chatno;
		if(service.lastReadChatNo(roomno, loginMember) != null) {
			last_read_chatno = service.lastReadChatNo(roomno, loginMember);
		} else {
			last_read_chatno = 0;
		}
		if(service.seenMessage(last_read_chatno, roomno, loginMember)) {
			return ResponseEntity.ok().body(chatMessageList);
		}else {
			System.out.println("실패");
			return null;
		}
	}
	
	@PutMapping(value="update/{roomno}")
	public ResponseEntity<String> modify(@PathVariable("roomno")int roomno, HttpSession session){
		String loginMember = (String) session.getAttribute("loginUser");
		int last_read_chatno;
		if(service.lastReadChatNo(roomno, loginMember) != null) {
			last_read_chatno = service.lastReadChatNo(roomno, loginMember);
		} else {
			last_read_chatno = 0;
		}
		return service.seenMessage(last_read_chatno, roomno, loginMember) ?
				new ResponseEntity<String>(HttpStatus.OK) :
				new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("image")
    public ResponseEntity<String> handleFileUpload(@RequestParam("files") List<MultipartFile> files) {
        // 파일을 저장할 경로
        String uploadDir = "C:/springcoders_file/";
        System.out.println(files.size());
        String imgNames = "";
        for (MultipartFile file : files) {
            imgNames += service.chatImage(file) + "\\";
        }
        System.out.println(imgNames);
        return ResponseEntity.ok().body(imgNames);
    }
}
