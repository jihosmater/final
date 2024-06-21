package com.team.demo.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.team.demo.domain.dto.ChatMessageDTO;
import com.team.demo.domain.dto.ChatRoomDTO;
import com.team.demo.domain.dto.ChatUserDTO;
import com.team.demo.domain.dto.FileDTO;
import com.team.demo.mapper.ChatMessageMapper;
import com.team.demo.mapper.ChatRoomMapper;
import com.team.demo.mapper.ChatUserMapper;
import com.team.demo.mapper.FileMapper;


@Service
public class ChatServiceImpl implements ChatService{

	@Value("${file.dir}")
    private String saveFolder;
	
	@Autowired
    ChatRoomMapper roomMapper;
    @Autowired
    ChatMessageMapper chatMapper;
    @Autowired
    ChatUserMapper roomUserMapper;
    @Autowired
    private FileMapper fmapper;
	
	@Override
	public ChatRoomDTO chatRoomGet(int roomno) {
		return roomMapper.chatRoomGet(roomno);
	}

	@Override
	public boolean chatRoomInsert(String roomname, String userid) {
		return roomMapper.chatRoomInsert(roomname, userid) == 1;
	}

	@Override
	public boolean chatRoomDelete(int roomno) {
		return roomMapper.chatRoomDelete(roomno) == 1;
	}

	@Override
	public boolean addBannuser(int roomno, String userid) {
		return roomMapper.addBannuser(roomno, userid) == 1;
	}
	
	@Override
	public List<ChatRoomDTO> getAllRoomByUserId(String userid) {
		return roomMapper.getAllRoomByUserId(userid);
	}

	@Override
	public ChatRoomDTO chatRoomGetBynames(int roomno, String loginMember) {
		return roomMapper.chatRoomGetBynames(roomno, loginMember);
	}
	
	@Override
	public int lastInsertRoomId() {
		return roomMapper.lastInsertRoomId();
	}
	
	@Override
	public List<ChatUserDTO> getAllUserRoomByUserId(String userid) {
		return roomUserMapper.getAllUserRoomByUserId(userid);
	}
	
	public boolean CheckUser(int roomno, String userid) {
		return roomUserMapper.CheckUser(roomno, userid) != null;
	}
	
	@Override
	public boolean chatUserInsert(int roomno, String roomname, String userid) {
		return roomUserMapper.chatUserInsert(roomno, roomname, userid);
	}

	@Override
	public boolean chatUserDelete(int roomno, String userid) {
		return roomUserMapper.chatUserDelete(roomno, userid);
	}

	@Override
	public List<ChatMessageDTO> chatMessageList(int roomno) {
		return chatMapper.chatMessageList(roomno);
	}

	@Override
	public boolean chatMessageInsert(ChatMessageDTO chatMessage) {
		return chatMapper.chatMessageInsert(chatMessage) == 1;
	}

	@Override
	public boolean chatMessageDelete(int chatno, String userid) {
		return chatMapper.chatMessageDelete(chatno, userid) == 1;
	}

	@Override
	public ChatMessageDTO lastMessage(int roomno) {
		return chatMapper.lastMessage(roomno);
	}

	@Override
	public int unseenMessage(String userid, int roomno) {
		Integer  unseenMessage = chatMapper.unseenMessage(userid, roomno);
		if(unseenMessage == null) {
			return 0;
		}
		return chatMapper.unseenMessage(userid, roomno);
	}

	@Override
	public Integer lastReadChatNo(int roomno, String loginMember) {
		return chatMapper.lastReadChatNo(roomno, loginMember);
	}

	@Override
	public boolean seenMessage(int last_read_chatno, int roomno, String loginMember) {
		return chatMapper.seenMessage(last_read_chatno, roomno, loginMember) == 1;
	}

	@Override
	public String chatImage(MultipartFile file) {
		if(file == null) {
            return null;
        }
		else {
            //방금 등록한 게시글 번호
            boolean flag = false;

            String orgname = file.getOriginalFilename();
            System.out.println(orgname);
            //5
            int lastIdx = orgname.lastIndexOf(".");
            //.png
            String extension = orgname.substring(lastIdx);

            LocalDateTime now = LocalDateTime.now();
            String time = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

            //20240502162130141랜덤문자열.png
            String systemname = time+ UUID.randomUUID() +extension;

            //실제 생성될 파일의 경로
            //D:/0900_GB_JDS/7_spring/file/20240502162130141랜덤문자열.png
            String path = saveFolder+systemname;

            //File DB 저장
            FileDTO fdto = new FileDTO();
            fdto.setOrgname(orgname);
            fdto.setSystemname(systemname);
            fdto.setFileid("chat");

            flag = fmapper.insertFile(fdto) == 1;

            //실제 파일 업로드
            try {
				file.transferTo(new File(path));
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            if(!flag) {
                //업로드했던 파일 삭제, 게시글 데이터 삭제, 파일 data 삭제, ...
                return null;
            }
            return systemname;
        }
	}

	@Override
	public ChatMessageDTO lastImage(int roomno) {
		return chatMapper.lastImage(roomno);
	}

	@Override
	public ChatMessageDTO lastImoticon(int roomno) {
		return chatMapper.lastImoticon(roomno);
	}

	@Override
	public int countDuplicateUsers(String roomname, String adminid, String userid) {
		return roomUserMapper.countDuplicateUsers(roomname, adminid, userid);
	}
}
