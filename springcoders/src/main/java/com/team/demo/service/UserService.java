package com.team.demo.service;

import com.team.demo.domain.dto.*;

import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface UserService {
	
	boolean join(UserDTO user);

	boolean apijoin(UserDTO user);

	boolean login(String userid, String userpw);

	UserDTO checkId(String userid);

	boolean deleteUser(String userid);

	boolean leaveId(String userid);
	
	UserDTO checkPN(String userphone);

	String randpass();

	boolean usercoinAdd(String userid, int payment_coin);

	List<UserDTO> getUserContainChatUser(int roomno);

	UserDTO checkemail(String useremail);

	UserDTO findByUserId(String useremail);

	boolean createByPw(String userid, String useremail);

	UserDTO getRandomPw(String userid, String useremail);
	
	UserDTO loginMatchId(String userid, String userpw);

	UserDTO getUserinfo(String userid);

	boolean chargeCoin(ChargeInfo chargeInfo);
	
	boolean checkPw(String userid, String userpw);
	
	boolean checkEmail(String userElement);
	
	boolean checkPhone(String userElement);
	
	boolean userModify(String userid, String userpw, String userElement,
			String modifyTypeValue);
	
	boolean fileReg(String userid, String systemName);
	
	boolean addrmodify(String userid, String userpw, String zipcode, String addr, String addrdetail, String addretc);
	
	int fileTableUpdate(FileDTO file);
	
	boolean userDeleteFile(String systemname);
	
	List<UserDTO> searchUserList(String userid);

//	장소 좋아요,좋아요 취소 시작
		boolean unlikePlace(String userid, long placeid);
		boolean likePlace(String userid, long placeid);
		boolean checkLikeStatus(String loginUser, long placeid);

	List<PlaceDTO> getLikedPlaces(String userId);
	
	FileDTO getFileByPlaceId(String placeid);

//	장소 좋아요,좋아요 취소 끝

	List<Map<String, Object>> getUserPurchasedBoards(String userid);

	List<PlanDTO> myPlanList(String loginUser, int startrow);

	List<UserDTO> getUserListByPlannum(long plannum);
}