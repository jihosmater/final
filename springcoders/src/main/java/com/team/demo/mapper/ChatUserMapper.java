package com.team.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.team.demo.domain.dto.ChatUserDTO;

@Mapper
public interface ChatUserMapper {
	List<ChatUserDTO> getAllUserRoomByUserId(String userid);
	ChatUserDTO CheckUser(@Param("roomno")int roomno, @Param("userid") String userid);
	boolean chatUserInsert(@Param("roomno")int roomno, @Param("roomname")String roomname, @Param("userid") String userid);
	boolean chatUserDelete(@Param("roomno")int roomno, @Param("userid") String userid);
	int countDuplicateUsers(@Param("roomname")String roomname, @Param("adminid") String adminid, @Param("userid") String userid);
}
