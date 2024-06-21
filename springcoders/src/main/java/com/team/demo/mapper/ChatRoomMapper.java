package com.team.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.team.demo.domain.dto.ChatRoomDTO;

@Mapper
public interface ChatRoomMapper {
	List<ChatRoomDTO> getAllRoomByUserId(String userid);
	ChatRoomDTO chatRoomGet(int roomno);
	int chatRoomInsert(@Param("roomname") String roomname, @Param("userid") String userid);
	int chatRoomDelete(int roomno);
	int addBannuser(@Param("roomno") int roomno, @Param("userid") String userid);
	ChatRoomDTO chatRoomGetBynames(@Param("roomno") int roomno, @Param("loginMember") String loginMember);
	int lastInsertRoomId();
}
