package com.team.demo.mapper;

import com.team.demo.domain.dto.ChargeInfo;
import com.team.demo.domain.dto.FileDTO;
import com.team.demo.domain.dto.PlaceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.team.demo.domain.dto.UserDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
//<<<<<<< HEAD
//	//C
//	int insertUser(UserDTO user);
//	//R
//	UserDTO getUserById(String userid);
//	//U
//	int updateUser(UserDTO user);
//	//D
//	int deleteUser(String userid);
//	
//	UserDTO getUserByEmail(String useremail);
//
//	UserDTO findByUserId(String useremail);
//
//	boolean CreateByPw(String userid, String useremail);
//
//	UserDTO getUserrandpw(String userid, String useremail);
//	
//	// UserDTO findByUserId(String useremail);
//
//	// UserDTO findByUserPw(String userid, String useremail);
//}
//=======
   //C
   int insertUser(UserDTO user);
   //R
   UserDTO getUserById(String userid);
   //U
   int updateUser(UserDTO user);
   //D
   int deleteUser(String userid);

   // usercoin충전
   int usercoinAdd (String userid, int payment_coin);

   // 즐겨찾기 장소 추가
    boolean addFavoriteList(@Param("userid")String userid, @Param("placeid")long placeid);

   int checkFavoritePlace(@Param("userid")String userid, @Param("placeid")long placeid);

   //chatting
   List<UserDTO> getUserContainChatUser(int roomno);

   UserDTO getUserByEmail(String useremail);

   UserDTO findByUserId(String useremail);

   boolean createByPw(String userid, String useremail);
   
   UserDTO loginMatchId(String userid, String userpw);

   UserDTO getUserrandpw(String userid, String useremail);

   boolean chargeCoin(ChargeInfo chargeInfo);
   
   UserDTO checkPN(String userphone);

   List<UserDTO> searchUserList(String userid);
   
   // modify
   boolean checkEmail(String userElement);
   
   boolean checkPhone(String userElement);
   
   boolean userModify(String userid, String userpw, String userElement, String modifyTypeValue);
   
   boolean fileReg(String userid, String systemName);
   
   boolean addrmodify(String userid, String userpw, String zipcode, String addr, String addrdetail, String addretc);
    
   boolean checkPw(String userid, String userpw);

   boolean insertUserPlaceLike(String userid, long placeid);
   
   boolean deleteUserPlaceLike(String userid, long placeid);
   
   boolean checkUserPlaceLike(String userid, long placeid);

   boolean deleteProfile(String systemname);
   
   List<Map<String, Object>> getUserPurchasedBoards(String userid);

    List<UserDTO> getUserListByPlannum(long plannum);
    int userCnt();
}
