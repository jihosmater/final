package com.team.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.team.demo.domain.dto.ChatMessageDTO;

@Mapper
public interface ChatMessageMapper {
	List<ChatMessageDTO> chatMessageList(int roomno);
	int chatMessageInsert(ChatMessageDTO chatMessage);
	int chatMessageDelete(@Param("chatno") int chatno, @Param("userid") String userid);
	ChatMessageDTO lastMessage(int roomno);
	Integer unseenMessage(String userid, int roomno);
	Integer lastReadChatNo(@Param("roomno")int roomno, @Param("loginMember")String loginMember);
	int seenMessage(@Param("last_read_chatno")int last_read_chatno, @Param("roomno")int roomno, @Param("loginMember")String loginMember);
	ChatMessageDTO lastImage(int roomno);
	ChatMessageDTO lastImoticon(int roomno);
}
