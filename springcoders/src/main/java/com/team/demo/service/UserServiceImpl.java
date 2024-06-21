package com.team.demo.service;


import com.team.demo.domain.dto.*;
import com.team.demo.mapper.*;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper umapper;
	
	@Autowired
	private FileMapper fmapper;

	@Autowired
	private PlaceMapper pmapper;
	
	@Autowired
	private BoardMapper bmapper;
	
	@Autowired
	private PaymentMapper pymapper;
	
	@Autowired
	private ReplyMapper rmapper;

	@Autowired
	private PlanMapper planMapper;

	@Override
	public boolean join(UserDTO user) {
		return umapper.insertUser(user) == 1;
	}

	// public UserDTO findByUserId(String useremail){
	// return umapper.findByUserId(useremail);
	// }
	// public UserDTO findByUserPw(String userid, String useremail){
	// return umapper.findByUserPw(userid, useremail);
	// }
	@Override
	public boolean apijoin(UserDTO user) {
		return umapper.insertUser(user) == 1;
	}

	@Override
	public boolean login(String userid, String userpw) {
		UserDTO user = umapper.getUserById(userid);
		System.out.println(userid);
		System.out.println(userpw);
		System.out.println(user);
		if (user != null) {
			if (user.getUserpw().equals(userpw)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public UserDTO findByUserId(String useremail) {
		return umapper.findByUserId(useremail);
	}
	@Override
	public UserDTO checkPN(String userphone){
		return umapper.checkPN(userphone);
	}

	@Override
	public boolean createByPw(String userid, String useremail) {
		return umapper.createByPw(userid, useremail);
	}

	@Override
	public UserDTO getRandomPw(String userid, String useremail) {
		return umapper.getUserrandpw(userid, useremail);
	}
	@Override
	   public UserDTO loginMatchId(String userid, String userpw) {
	      return umapper.loginMatchId(userid,userpw);
	   }

	@Override
	public UserDTO checkId(String userid) {
		return umapper.getUserById(userid);
	}

	@Override
	public boolean leaveId(String userid) {
		return umapper.deleteUser(userid) == 1;
	}

	@Override
	public boolean deleteUser(String userid) {	
		bmapper.UserDelete(userid);
		pymapper.userDeletePayment(userid);
		
		UserDTO user =  umapper.getUserById(userid);
		String userfile = user.getUserprofile();
		
		fmapper.userDeleteFile(userfile);
		
		rmapper.userDeleteReply(userid);
		umapper.deleteUser(userid);
		return false;
	}

	public String randpass() {
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		SecureRandom random = new SecureRandom();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < 10; i++) {
			// 무작위로 문자열의 인덱스 반환
			int index = random.nextInt(chars.length());
			// index의 위치한 랜덤값
			sb.append(chars.charAt(index));
		}

		return sb.toString();
	}

	@Override
	public boolean usercoinAdd(String userid, int payment_coin) {
		if (umapper.usercoinAdd(userid, payment_coin) != 1) {
			return false;
		}
		return true;
	}

	@Override
	public List<UserDTO> getUserContainChatUser(int roomno) {
		return umapper.getUserContainChatUser(roomno);
	}

	@Override
	public UserDTO checkemail(String useremail) {
		return umapper.getUserByEmail(useremail);
	}

	@Override
	public UserDTO getUserinfo(String userid) {
		return umapper.getUserById(userid);
	}

	@Override
	public boolean chargeCoin(ChargeInfo chargeInfo) {
		return umapper.chargeCoin(chargeInfo);
	}
	public List<UserDTO> searchUserList(String userid) {
		return umapper.searchUserList(userid);
	}


	@Override
	public boolean checkPw(String userid, String userpw) {
		return umapper.checkPw(userid, userpw);
	}

	@Override
	public boolean userModify(String userid, String userpw, String userElement, String modifyTypeValue) {
		return umapper.userModify(userid, userpw, userElement, modifyTypeValue);
	}

	@Override
	public boolean checkEmail(String userElement) {
		return umapper.checkEmail(userElement);
	}

	@Override
	public boolean checkPhone(String userElement) {
		return umapper.checkPhone(userElement);
	}

	@Override
	public boolean fileReg(String userid, String systemName) {
		return umapper.fileReg(userid, systemName);
	}

	@Override
	public int fileTableUpdate(FileDTO file) {
		return fmapper.insertFile(file);
	}

	@Override
	public boolean addrmodify(String userid, String userpw, String zipcode, String addr, String addrdetail,
			String addretc) {
		return umapper.addrmodify(userid, userpw, zipcode, addr, addrdetail, addretc);
	}
	
//	장소 좋아요, 좋아요 취소 시작
	@Override
	public boolean checkLikeStatus(String userid, long placeid) {
		return umapper.checkUserPlaceLike(userid, placeid);
	}

	@Override
	public boolean likePlace(String userid, long placeid) {
		return umapper.insertUserPlaceLike(userid, placeid);
	}


	@Override
	public boolean unlikePlace(String userid, long placeid) {
		return umapper.deleteUserPlaceLike(userid, placeid);
	}
	@Override
	public List<PlaceDTO> getLikedPlaces(String userid) {
		// UserServiceImpl에서 PlaceMapper를 이용하여 사용자가 좋아하는 장소 정보를 가져옴
		return pmapper.findFavoritesByUserId(userid);
	}

	@Override
	public FileDTO getFileByPlaceId(String fileid) {
		// UserServiceImpl에서 FileMapper를 이용하여 해당 장소의 파일 정보를 가져옴
		return fmapper.getFileByFileid(fileid);
	}
	
	@Override
	public boolean userDeleteFile(String systemname) {
		return fmapper.userDeleteFile(systemname);
	}

//	장소 좋아요, 좋아요 취소 끝
@Override
public List<Map<String, Object>> getUserPurchasedBoards(String userid) {
	// MyBatis Mapper 메서드를 호출하여 로그인한 사용자가 구매한 게시물 목록을 가져옵니다
	return umapper.getUserPurchasedBoards(userid);
}

	@Override
	public List<PlanDTO> myPlanList(String loginUser, int startrow) {
		return planMapper.getPlanListByUserid(loginUser,startrow);
	}

	@Override
	public List<UserDTO> getUserListByPlannum(long plannum) {
		return umapper.getUserListByPlannum(plannum);
	}

}
