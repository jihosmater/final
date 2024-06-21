package com.team.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.team.demo.domain.dto.ChatMessageDTO;
import com.team.demo.domain.dto.ChatRoomDTO;
import com.team.demo.domain.dto.ChatUserDTO;

public interface ChatService{
	//chatroom
	public ChatRoomDTO chatRoomGet(int roomno);
	public boolean chatRoomInsert(String roomname, String userid);
	public boolean chatRoomDelete(int roomno);
	public boolean addBannuser(int roomno, String userid);
	public List<ChatRoomDTO> getAllRoomByUserId(String userid);
	public ChatRoomDTO chatRoomGetBynames(int roomno, String loginMember);
	public int lastInsertRoomId();
	
	//chatuser
	public List<ChatUserDTO> getAllUserRoomByUserId(String userid);
	public boolean CheckUser(int roomno, String userid);
	public boolean chatUserInsert(int roomno, String roomname, String userid);
	public boolean chatUserDelete(int roomno, String userid);
	public int countDuplicateUsers(String roomname, String adminid, String userid);
	
	
	//chatmessage
	public List<ChatMessageDTO> chatMessageList(int roomno);
	public boolean chatMessageInsert(ChatMessageDTO chatMessage);
	public boolean chatMessageDelete(int chatno, String userid);
	public ChatMessageDTO lastMessage(int roomno);
	public int unseenMessage(String userid, int roomno);
	public Integer lastReadChatNo(int roomno, String loginMember);
	public boolean seenMessage(int last_read_chatno, int roomno, String loginMember);
	public String chatImage(MultipartFile file);
	public ChatMessageDTO lastImage(int roomno);
	public ChatMessageDTO lastImoticon(int roomno);
}
