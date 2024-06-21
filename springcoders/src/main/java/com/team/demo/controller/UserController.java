package com.team.demo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.team.demo.domain.dto.*;
import com.team.demo.service.PaymentService;
import com.team.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.MessageListRequest;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.saml2.Saml2RelyingPartyProperties.AssertingParty.Verification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/user/*")
public class UserController {
    @Autowired
    private UserService service;
    @Autowired
    private PaymentService pservice;

    private Map<String, String> verificationCodes = new HashMap<>();

    final DefaultMessageService messageService;

    public UserController() {
        this.messageService = NurigoApp.INSTANCE.initialize("NCSFZYRC2A26BPQK", "VSFUIWYN1M8FR3OHP6JO1ZFWIDUWS2ZJ", "https://api.coolsms.co.kr");
    }

    @GetMapping("join")
    public void replace() {
    }
    @PostMapping("join")
    public String join(UserDTO user, HttpServletResponse resp) {
        // 회원가입 처리
        if (service.join(user)) {
            // Cookie cookie = new Cookie("joinid", user.getUserid());
            // cookie.setPath("/");
            // cookie.setMaxAge(60);
            // resp.addCookie(cookie);

            return "/user/login";
        } else {
            // 회원가입 실패 시 처리
            return null;
        }
    }

    @GetMapping("checkId")
    @ResponseBody
    public String checkId(String userid) {
        if (!(service.checkId(userid) == null)) {
            System.out.println("o");
            return "O";
        } else {
            System.out.println("x");
            return "X";
        }
    }

    @GetMapping("findByUserId")
    @ResponseBody
    public String findByUserId(String useremail) {
        UserDTO user = service.findByUserId(useremail);
        System.out.println(user);
        if (user != null) {
            return user.getUserid();
        } else {
            return null; // 해당 이메일을 가진 사용자를 찾을 수 없는 경우 빈 문자열 반환
        }
    }
    @GetMapping("checkemail")
    @ResponseBody
    public String checkemail(String useremail) {
        if (!(service.checkemail(useremail) == null)) {
            System.out.println("o");
            return "O";
        } else {
            System.out.println("x");
            return "X";
        }
    }

    @GetMapping("createByPw")
    @ResponseBody
    public String CreateByPw(String userid, String useremail, HttpServletResponse resp) {
        System.out.println("에효");
        Boolean user = service.createByPw(userid, useremail);
        if (service.createByPw(userid, useremail)) {
            System.out.println("o"); // 성공적으로 업데이트됨을 나타내는 논리적인 값을 반환
            return "O";
        } else {
            System.out.println("x"); // 업데이트 실패를 나타내는 논리적인 값을 반환
            return "X";
        }
    }

    @GetMapping("getRandomPw")
    @ResponseBody
    public String getRandomPw(String userid, String useremail) {
        UserDTO user = service.getRandomPw(userid, useremail);
        System.out.println(user);
        if (user != null) {
            return user.getUserpw();
        } else {
            return ""; // 해당 이메일을 가진 사용자를 찾을 수 없는 경우 빈 문문자열 반환
        }
    }
    @GetMapping("checkPN")
    @ResponseBody
    public String checkPN(String userphone){
        if (!(service.checkPN(userphone) == null)) {
            System.out.println("o");
            return "O";
        } else {
            System.out.println("x");
            return "X";
        }
    }


    @GetMapping("loginMatchId")
    @ResponseBody
    public String loginMatchId(String userid, String userpw) {
        UserDTO user = service.loginMatchId(userid, userpw);
        System.out.println(user);
        if (user != null) {
            return user.getUserid();
        } else {
            return "";
        }
    }

    @GetMapping("UserDelete")
    public void UserDelete() {
    }

    @PostMapping("UserDelete")
    public String UserDelete(@RequestParam String userid) {
        if (service.deleteUser(userid)) {
            System.out.println("회원탈퇴 실패");
        } else {
            System.out.println("회원탈퇴 성공");
        }
        return "redirect:/";
    }

    @GetMapping("JoinCategory")
    public String joinCategory() {
        return "user/JoinCategory";
    }
    @PostMapping("apijoin")
    public String apijoin(UserDTO user, HttpServletResponse resp) {
        // 회원가입 처리
        if (service.join(user)) {
            Cookie cookie = new Cookie("joinid", user.getUserid());
            cookie.setPath("/");
            cookie.setMaxAge(60);
            resp.addCookie(cookie);
        } else {
            // 회원가입 실패 시 처리
        }
        return "redirect:/";
    }

    @GetMapping("findid")
    public String findid(){
        return "user/findid";
    }


    @GetMapping("findpw")
    public String findpw(){
        return "user/findpw";
    }

    @PostMapping("phonecheck")
    public ResponseEntity<SingleMessageSentResponse> sendOne(String userid, String phonechecknum) {

        UserDTO user =  service.getUserinfo(userid);
        String userphone = user.getUserphone();
        SingleMessageSentResponse response = null;

        if(userphone.equals(phonechecknum)) {

            System.out.println("휴대폰번호가 일치합니다");

            Message message = new Message();
            Random rand = new Random();
            String numstr = "";
            for(int i = 0; i < 7; i++) {
                String ran = Integer.toString(rand.nextInt(10));
                numstr += ran;
            }

            System.out.println("수신자 번호 : "+phonechecknum);
            System.out.println("인증번호 : "+numstr);

            String myphone = "01090698954";

            message.setFrom(myphone);
            message.setTo(phonechecknum);
            message.setText("PLANZ 휴대폰 인증번호 : "+numstr);

            String phonecheckKey = "phonecheckKey";

            verificationCodes.put(phonecheckKey, numstr);

            response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
            System.out.println("리스폰 확인 : "+response);

        } else {
            System.out.println("전화번호가 다릅니다");
            throw new IllegalArgumentException("전화번호가 일치하지 않습니다.");
        }
        return  ResponseEntity.ok(response);
    }
    @PostMapping("login")
    public String login(String userid, String userpw, HttpServletRequest req) {
        HttpSession session = req.getSession();
        System.out.println(userid + userpw);
        if (service.login(userid, userpw)) {
            session.setAttribute("loginUser", userid);
            System.out.println(session.getAttribute("loginUser"));

            if (userid.equals("manager")) {
                return "redirect:/manager/agree"; // manager는 관리자 페이지로
            } else {
                return "redirect:/"; // 일반 사용자는 메인 페이지로
            }
        } else {
            System.out.println("실패");
            // 로그인 실패 시 처리
        }
        return "redirect:/";
    }


    @PostMapping("/modify")
    public String UserModify(@RequestBody Map<String, String> request) {

        String userid = request.get("userid");
        String userpw = request.get("userpw");
        String userElement = request.get("userElement");
        String modifyTypeValue = request.get("modifyTypeValue");
        String checknum = request.get("checknum");
        String phonecheckKey = "phonecheckKey";
        String numstr = verificationCodes.get(phonecheckKey);

        System.out.println("유저아이디 : "+userid);
        System.out.println("유저pw : "+userpw);
        System.out.println("바꿀값 : "+userElement);
        System.out.println("타입 : "+modifyTypeValue);
        System.out.println("인증번호입력값 : "+checknum);
        System.out.println("인증번호 설정값 : "+numstr);

        boolean returnpw = service.checkPw(userid, userpw);
        System.out.println("torf : "+returnpw);

        if(returnpw) {

            if ("pw".equals(modifyTypeValue)) {

                System.out.println("비밀번호 변경");

                if(numstr.equals(checknum)) {
                    service.userModify(userid, userpw, userElement, modifyTypeValue);
                } else {
                    System.out.println("인증번호가 일치하지 않습니다.");
                    throw new BadRequestException("인증번호가 일치하지 않습니다.");
                }


            } else if ("phone".equals(modifyTypeValue)) {

                System.out.println("전화번호 변경");

                if(!service.checkPhone(userElement)) {

                    service.userModify(userid, userpw, userElement, modifyTypeValue);

                } else {
                    System.out.println("전화번호가 중복되었습니다");
                }

            } else if ("email".equals(modifyTypeValue)) {

                System.out.println("이메일 변경");

                if(!service.checkEmail(userElement)) {

                    service.userModify(userid, userpw, userElement, modifyTypeValue);

                } else {
                    System.out.println("이메일이 중복되었습니다");
                }

            } else if ("addr".equals(modifyTypeValue)) {

                service.userModify(userid, userpw, userElement, modifyTypeValue);

            } else {
                System.out.println("수정타입이 유효하지않습니다.");
            }

        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }
        return "redirect:/";
    }

    @DeleteMapping("snsUserDelete")
    public ResponseEntity<?> snsUserDelete(@RequestBody Map<String, String> request) {
        String userid = request.get("userid");

        System.out.println("SNS로그인유저 회원탈퇴 컨트롤러 들어옴");

        System.out.println("파라미터 확인 : "+userid);

        UserDTO user = service.getUserinfo(userid);

        System.out.println("프로필파일 확인 : "+user.getUserprofile());

        if(user.getUserprofile() == null || user.getUserprofile() == "") {

            System.out.println("프로필사진 미등록자");
            service.deleteUser(userid);
            System.out.println("회원탈퇴완료");
            return ResponseEntity.ok("회원탈퇴가 성공적으로 처리되었습니다.");

        } else {

            System.out.println("프로필사진 등록자");
            String existingProfileImagePath = "C:/springcoders_file/"+user.getUserprofile();
            File existingProfileImage = new File(existingProfileImagePath);

            if(existingProfileImage.exists()) {
                existingProfileImage.delete();
                System.out.println("회원탈퇴 프로필사진 삭제");
                service.deleteUser(userid);
                System.out.println("회원탈퇴완료");
                return ResponseEntity.ok("회원탈퇴와 함께 프로필 이미지가 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("프로필 이미지 삭제 중 오류가 발생했습니다.");
            }
        }
    }

    @DeleteMapping("userDelete")
    public ResponseEntity<?> userDelete(@RequestBody Map<String, String> request) {

        System.out.println("회원탈퇴 컨트롤러 들어옴");

        String userid = request.get("userid");
        String phonechecknum = request.get("phonechecknum");
        String allcheck = request.get("allcheck");
        String checknum = request.get("checknum");
        String phonecheckKey = "phonecheckKey";
        String numstr = verificationCodes.get(phonecheckKey);


        System.out.println("유저아이디 : "+userid);
        System.out.println("유저핸드폰번호 : "+phonechecknum);
        System.out.println("현재사용비밀번호 : "+allcheck);
        System.out.println("인증번호 : "+checknum);

        boolean returnpw = service.checkPw(userid, allcheck);

        if(numstr.equals(checknum)) {

            if(returnpw) {

                UserDTO user = service.getUserinfo(userid);

                if(user.getUserphone().equals(phonechecknum)) {

                    if(user.getUserprofile() == null || user.getUserprofile() == "") {

                        System.out.println("프로필사진 미등록자");
                        service.deleteUser(userid);
                        System.out.println("회원탈퇴 완료");
                        return ResponseEntity.ok("회원탈퇴가 성공적으로 처리되었습니다.");

                    } else {

                        System.out.println("프로필사진 등록자");
                        String existingProfileImagePath = "C:/springcoders_file/"+user.getUserprofile();
                        File existingProfileImage = new File(existingProfileImagePath);

                        if(existingProfileImage.exists()) {
                            existingProfileImage.delete();
                            System.out.println("회원탈퇴 프로필사진 삭제");
                        }

                        service.deleteUser(userid);
                        System.out.println("회원탈퇴 완료");
                        return ResponseEntity.ok("회원탈퇴가 성공적으로 처리되었습니다.");
                    }

                } else {
                    System.out.println("휴대폰번호가 일치하지 않습니다.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("핸드폰 번호가 일치하지 않습니다.");
                }
            } else {
                System.out.println("비밀번호가 일치하지 않습니다.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
            }
        } else {
            System.out.println("인증번호가 일치하지 않습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증번호가 일치하지 않습니다.");
        }
    }

    @PostMapping("addrmodify")
    public String addrmodify(@RequestBody Map<String, String> request) {

        System.out.println("addrmodify 포스트매핑");

        String userid = request.get("userid");
        String userpw = request.get("userpw");
        String zipcode = request.get("zipcode");
        String addr = request.get("addr");
        String addrdetail = request.get("addrdetail");
        String addretc = request.get("addretc");

        boolean returnpw = service.checkPw(userid, userpw);

        System.out.println("유저아이디 : "+userid);
        System.out.println("유저패스워드 : "+userpw);
        System.out.println("유저우편번호 : "+zipcode);
        System.out.println("유저주소 : "+addr);
        System.out.println("유저상세주소 : "+addrdetail);
        System.out.println("유저참고사항 : "+addretc);

        if(returnpw) {
            System.out.println("비밀번호가 일치합니다");

            service.addrmodify(userid, userpw, zipcode, addr, addrdetail, addretc);
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
            throw new BadRequestException("비밀번호가 일치하지 않습니다");
        }

        return "redirect:/";
    }

    @PostMapping ("snsUserProfile")
    public String snsUserProfile(@RequestParam("userid") String userid,
                                 @RequestParam("files") MultipartFile file) {
        System.out.println("SNS로그인유저 프로필 변경컨트롤");

        UserDTO user =  service.checkId(userid);

        if(userid.equals(user.getUserid())) {

            if(!file.isEmpty()) {
                System.out.println("파일 이름 : "+file.getOriginalFilename());

                String orgFileName = file.getOriginalFilename();

                // 실제 파일 경로
                String filepath = "C:/springcoders_file/";
                System.out.println("실제파일경로확인 : "+filepath);

                // 파일 저장 디렉토리 확인 및 생성
                File dir = new File(filepath);
                if(!dir.exists()) {
                    dir.mkdir();
                }

                String orgname = orgFileName;
                String systemName = "";
                // 저장할 파일의 전체 경로
                File dest = new File(filepath+orgFileName);
                int count = 2;

                // 실제 경로상에 파일명이 같은 파일이 있는지
                while (dest.exists()) {
                    int dotIndex = orgFileName.lastIndexOf(".");
                    String baseName = orgFileName.substring(0, dotIndex);
                    String extension = orgFileName.substring(dotIndex);
                    systemName = baseName + "(" + count + ")" + extension;
                    dest = new File(filepath + systemName);
                    count++;
                }

                // 해당 사용자의 프로필 이미지가 이미 존재하는지 확인
                UserDTO userinfo =  service.getUserinfo(userid);
                String profile = userinfo.getUserprofile();

                if(profile == null || profile == "") {
                    System.out.println("프로필사진 첫 등록자");
                } else {
                    System.out.println("프로필사진이 이미등록되어 있는 유저");
                    String existingProfileImagePath = "C:/springcoders_file/" + profile;
                    File existingProfileImage = new File(existingProfileImagePath);

                    if(existingProfileImage.exists()) {
                        existingProfileImage.delete();
                        System.out.println("기존 프로필 이미지 삭제 완료");
                        String imageFile = userinfo.getUserprofile();
                        service.userDeleteFile(imageFile);
                    }
                }

                try {
                    // 파일을 실제 경로에 저장
                    file.transferTo(dest);
                    System.out.println("파일이 성공적으로 저장되었습니다 " + dest.getAbsolutePath());

                    System.out.println("오리지날 : "+orgFileName);
                    System.out.println("시스템 : "+systemName);

                    FileDTO fileinfo = new FileDTO();

                    String fileid = "userid_"+userid;

                    if(systemName == "" || systemName == null) {
                        fileinfo.setSystemname(orgname);
                        fileinfo.setOrgname(orgname);
                        fileinfo.setFileid(fileid);
                    } else {
                        fileinfo.setSystemname(systemName);
                        fileinfo.setOrgname(orgname);
                        fileinfo.setFileid(fileid);
                    }

                    // 파일을 DB에 저장
                    if(systemName == "" || systemName == null) {
                        System.out.println("시스템이름이 없을때");
                        service.fileReg(userid, orgFileName);
                        service.fileTableUpdate(fileinfo);
                    } else {
                        System.out.println("시스템이름이 있을때");
                        service.fileReg(userid, systemName);
                        service.fileTableUpdate(fileinfo);
                    }

                } catch (Exception e) {
                    System.out.println("파일 저장 중 오류가 발생했습니다.");
                    e.printStackTrace();
                }

            } else {
                System.out.println("파일이 업로드되지 않았습니다.");
            }
        } else {
            System.out.println("아이디가 확인되지 않습니다");
        }
        return "redirect:/";
    }

    @PostMapping("profile")
    public String profile(@RequestParam("userid") String userid,
                          @RequestParam("userpw") String userpw,
                          @RequestParam("files") MultipartFile file) {
        System.out.println("profile 포스트매핑");

        boolean returnpw = service.checkPw(userid, userpw);

        System.out.println("유저아이디 : "+userid);
        System.out.println("유저패스워드 : "+userpw);
        System.out.println("유저파일 : "+file);

        if(returnpw) {
            System.out.println("비밀번호가 일치합니다");
            System.out.println("파일업로드 유저아이디 : "+userid);
            System.out.println("파일업로드 유저패스워드 : "+userpw);

            if(!file.isEmpty()) {
                System.out.println("파일 이름 : "+file.getOriginalFilename());

                String orgFileName = file.getOriginalFilename();

                // 실제 파일 경로
                String filepath = "C:/springcoders_file/";
                System.out.println("실제파일경로확인 : "+filepath);

                // 파일 저장 디렉토리 확인 및 생성
                File dir = new File(filepath);
                if(!dir.exists()) {
                    dir.mkdir();
                }

                String orgname = orgFileName;
                String systemName = "";
                // 저장할 파일의 전체 경로
                File dest = new File(filepath+orgFileName);
                int count = 2;

                // 실제 경로상에 파일명이 같은 파일이 있는지
                while (dest.exists()) {
                    int dotIndex = orgFileName.lastIndexOf(".");
                    String baseName = orgFileName.substring(0, dotIndex);
                    String extension = orgFileName.substring(dotIndex);
                    systemName = baseName + "(" + count + ")" + extension;
                    dest = new File(filepath + systemName);
                    count++;
                }

                // 해당 사용자의 프로필 이미지가 이미 존재하는지 확인
                UserDTO user =  service.getUserinfo(userid);
                String profile = user.getUserprofile();

                if(profile == null || profile == "") {
                    System.out.println("프로필사진 첫 등록자");
                } else {
                    System.out.println("프로필사진이 이미등록되어 있는 유저");
                    String existingProfileImagePath = "C:/springcoders_file/" + profile;
                    File existingProfileImage = new File(existingProfileImagePath);

                    if(existingProfileImage.exists()) {
                        existingProfileImage.delete();
                        System.out.println("기존 프로필 이미지 삭제 완료");
                        String imageFile = user.getUserprofile();
                        service.userDeleteFile(imageFile);
                    }
                }

                try {
                    // 파일을 실제 경로에 저장
                    file.transferTo(dest);
                    System.out.println("파일이 성공적으로 저장되었습니다 " + dest.getAbsolutePath());

                    System.out.println("오리지날 : "+orgFileName);
                    System.out.println("시스템 : "+systemName);

                    FileDTO fileinfo = new FileDTO();

                    String fileid = "userid_"+userid;

                    if(systemName == "" || systemName == null) {
                        fileinfo.setSystemname(orgname);
                        fileinfo.setOrgname(orgname);
                        fileinfo.setFileid(fileid);
                    } else {
                        fileinfo.setSystemname(systemName);
                        fileinfo.setOrgname(orgname);
                        fileinfo.setFileid(fileid);
                    }

                    // 파일을 DB에 저장
                    if(systemName == "" || systemName == null) {
                        System.out.println("시스템이름이 없을때");
                        service.fileReg(userid, orgFileName);
                        service.fileTableUpdate(fileinfo);
                    } else {
                        System.out.println("시스템이름이 있을때");
                        service.fileReg(userid, systemName);
                        service.fileTableUpdate(fileinfo);
                    }

                } catch (Exception e) {
                    System.out.println("파일 저장 중 오류가 발생했습니다.");
                    e.printStackTrace();
                }

            } else {
                System.out.println("파일이 업로드되지 않았습니다.");
            }
        } else {
            System.out.println("비밀번호가 일치하지 않습니다.");
            throw new BadRequestException("비밀번호가 일치하지 않습니다");
        }
        return "redirect:/";
    }

    @GetMapping("login")
    public String login(HttpSession session) {
        System.out.println("1");
        Object user = session.getAttribute("loginUser"); // 속성 이름 통일
        System.out.println("2");
        if (user != null) {
            session.getAttribute("loginUser");
            session.setMaxInactiveInterval(60 * 60 * 6);
            System.out.println(session.getAttribute("loginUser"));
            System.out.println("getLogin if문 1 ");
            return "/user/login"; // 로그인 후 홈 페이지로 리디렉션
        } else {
            System.out.println("getLogin else문");
            return "/user/login"; // 로그인 페이지로 이동
        }
    }
    @GetMapping("logout")
    public String logout(HttpServletRequest req) {
        req.getSession().invalidate();
        return "redirect:/";
    }

    @GetMapping("mypage")
    public String mypage(Model model, HttpSession session, String order) {

        String userid = (String) session.getAttribute("loginUser");

        System.out.println("로그인유저 : "+userid);
//        String userid = user.getUserid();

//       System.out.println("model = " + model);
        System.out.println("order = " + order);
        if(order != null && !order.equals("")){
            model.addAttribute("order", order);

        }
        List<Map<String, Object>> purchasedBoards = service.getUserPurchasedBoards(userid);

        // 모델에 구매한 게시물 목록을 추가
        model.addAttribute("purchasedBoards", purchasedBoards);

        // 세션값 저장
        // String userid = (String) session.getAttribute("loginUserId");

        // 세션에 로그인한 아이디가 없으면 로그인 페이지로 리다이렉트
//      if(userid == null) {
//         return "redirect:/login";
//      }

        // 테스토용 임의 apple설정

//       String userid = "apple";

        UserDTO user = service.getUserinfo(userid);
        UserDTO realuser = service.getUserinfo(userid);

        ChargeUserInfoDTO userinfo = new ChargeUserInfoDTO();

        userinfo.setUserid(user.getUserid());
        userinfo.setUserphone(user.getUserphone());
        userinfo.setUseremail(user.getUseremail());
        userinfo.setUsername(user.getUsername());
        model.addAttribute("userinfo", userinfo);

        List<FileDTO> filelist = new ArrayList<>();

        System.out.println("사용자 아이디 : "+user.getUserid());
        System.out.println("사용자 이름 : "+user.getUsername());
        System.out.println("사용자 전화번호 : "+user.getUserphone());
        System.out.println("사용자 이메일 : "+user.getUseremail());
        model.addAttribute("username", user.getUsername());

        // 사용자 아이디 가리기
        user.setUserid(maskUserId(user.getUserid()));

        System.out.println("마스킹 된 아이디 :" +	(maskUserId(user.getUserid())));

        // 사용자 이름 가리기
        user.setUsername(maskUserName(user.getUsername()));

        // 휴대폰 번호 가리기
        user.setUserphone(maskPhoneNumber(user.getUserphone()));

        // 이메일 가리기
        user.setUseremail(maskEmail(user.getUseremail()));



        model.addAttribute("user", user);
        model.addAttribute("realuser", realuser);

        //모델 확인용
        System.out.println("모델 값 :" + model.getAttribute("user"));
        model.addAttribute("user", user);
        model.addAttribute("userprofile", user.getUserprofile());;

        return "/user/mypage";
    }

    @PostMapping("/charge")
    @ResponseBody
    public String chargeCoin(@RequestBody ChargeInfo chargeInfo) {

        ChargeInfo chargeinfo = chargeInfo;
        String userid = chargeinfo.getUserid();
        String realuserid = userid.replace("\"", "");

        System.out.println("파라미터 확인 : "+userid);
        System.out.println("파라미터 재확인 : "+realuserid);

        chargeInfo.setUserid(realuserid);

        boolean result = service.chargeCoin(chargeInfo);
        boolean result2 = pservice.insertChargeInfo(chargeInfo);

        System.out.println("userservice 차지인포1 : "+chargeInfo.getUserid());
        System.out.println("userservice 차지인포2 : "+chargeInfo.getUsercoin());
        System.out.println(result);
        System.out.println(result2);

        return "redirect:/";
    }

    // 사용자 아이디 가리기 메서드
    private String maskUserId(String userid) {
        if(userid == null || userid.length() <= 1) {
            return userid;
        }

        char firstChar = userid.charAt(0);
        String maskedUserId = firstChar + "*".repeat(userid.length()-1);
        return maskedUserId;
    }

    // 사용자 이름 가리기 메서드
    private String maskUserName(String username) {
        if(username == null || username.length() <= 2) {
            return username;
        }

        char firstChar = username.charAt(0);
        char lastChar = username.charAt(username.length() -1);
        String maskUserName = firstChar + "*".repeat(username.length() -2) + lastChar;
        return maskUserName;
    }

    // 휴대폰 번호 가리기
    private String maskPhoneNumber(String phone) {
        if(phone == null || phone.length() != 11) {
            return phone;
        }

        return phone.substring(0, 3) + "-****-" + phone.substring(7);
    }

    // 이메일 가리기
    private String maskEmail(String email) {
        if(email == null || !email.contains("@")) {
            return email;
        }

        String[] parts = email.split("@");
        String localPart = parts[0];
        String domain = parts[1];
        if(localPart.length() <= 2) {
            return email;
        }
        char firstChar = localPart.charAt(0);
        char lastChar = localPart.charAt(localPart.length() -1);
        String maskedLocalPart = firstChar + "*".repeat(localPart.length() -2) + lastChar;
        return maskedLocalPart + "@" + domain;

    }


    // /place/create 뷰에서 아이디 찾기
    @PostMapping("searchId")
    public ResponseEntity<List<UserDTO>> searchId(@RequestParam String userid,HttpServletRequest req) {
        String loginUser = (String)req.getSession().getAttribute("loginUser");
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        List<UserDTO> list = service.searchUserList(userid);
        for(int i = 0;i<list.size();i++){
            if(list.get(i).getUserid().equals(loginUser)){
                list.remove(i);
                break;
            }
        }
        // user 리스트
        json.add("userList",gson.toJsonTree(list));
        String map = gson.toJson(json);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    // 비밀번호 오류시 오류상태 보내기
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
    }
    @PostMapping(value = "/checkLikeStatus", produces = "application/json;charset=utf-8" )
    public ResponseEntity<String> checkLikeStatus(@RequestParam long placeid, HttpServletRequest req) {
        System.out.println("체크좀 해보자 placeid = " + placeid);
        placeid = Long.parseLong("1000" + placeid);
        HttpSession session = req.getSession();
        String loginUser = (String)session.getAttribute("loginUser");
        if(service.checkLikeStatus(loginUser, placeid)) {
            System.out.println("체크좀 해볼까유? ");
//               존재하면 O
            return new ResponseEntity<String>("O", HttpStatus.OK);
        } else {
//                존재하지않으면 Y
            return new ResponseEntity<String>("Y", HttpStatus.OK);
        }

    }
    @PostMapping(value = "/like", produces = "application/json;charset=utf-8")
    public ResponseEntity<String> likePlace(@RequestParam long placeid, HttpServletRequest req) {
        placeid = Long.parseLong("1000" + placeid);
        HttpSession session = req.getSession();
        String loginUser = (String)session.getAttribute("loginUser");
        if (loginUser == null) {
            return new ResponseEntity<String>("X", HttpStatus.OK);
        } else {
            if(service.likePlace(loginUser, placeid)) {
                return new ResponseEntity<String>("O", HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Y", HttpStatus.OK);
            }
        }
    }

    @PostMapping(value = "/unlike", produces = "application/json;charset=utf-8")
    public ResponseEntity<String> unlikePlace(@RequestParam long placeid, HttpServletRequest req) {
        placeid = Long.parseLong("1000" + placeid);
        HttpSession session = req.getSession();
        String loginUser = (String)session.getAttribute("loginUser");
        if (loginUser == null) {
            return new ResponseEntity<String>("X", HttpStatus.OK);
        } else {
            if(service.unlikePlace(loginUser, placeid)) {
                return new ResponseEntity<String>("O", HttpStatus.OK);
            } else {
                return new ResponseEntity<String>("Y", HttpStatus.OK);
            }
        }
    }

    @PostMapping(value = "/favorites",produces = "application/json;charset=utf-8")
    public ResponseEntity<String> getLikedPlaces(HttpServletRequest req, Model model) {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        HttpSession session = req.getSession();
        String loginUser = (String)session.getAttribute("loginUser");

        // UserService를 통해 사용자가 좋아하는 장소 정보를 가져옴
        List<PlaceDTO> likedPlaces = service.getLikedPlaces(loginUser);
        List<String> placeImg = new ArrayList<>();
        // 각 장소에 대한 파일 정보를 가져와서 파일 이름만 추가
        for (PlaceDTO place : likedPlaces) {
            // UserService를 통해 해당 장소의 파일 정보를 가져옴

            FileDTO fileDTO = service.getFileByPlaceId("place_" + place.getPlaceid());
            // 파일 정보가 있는 경우에만 추가
            if (fileDTO != null) {
                placeImg.add(fileDTO.getSystemname());
            }
        }

        // 모델에 장소 정보 추가
        json.add("likedPlaces", gson.toJsonTree(likedPlaces));
        json.add("placeImg", gson.toJsonTree(placeImg));
        String map = gson.toJson(json);
        System.out.println("map = " + map);
        return new ResponseEntity<String>(map, HttpStatus.OK);
    }

    @PostMapping(value = "/buyerInfo",produces = "application/json;charset=utf-8")
    public ResponseEntity<String> buyerInfo(@RequestParam String userid, Model model) {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        json.add("buyer", gson.toJsonTree(service.getUserinfo(userid)));
        String buyer = gson.toJson(json);
        return new ResponseEntity<String>(buyer, HttpStatus.OK);
    }

    @PostMapping(value = "/myplan",produces = "application/json;charset=utf-8")
    public ResponseEntity<String> myplan(HttpServletRequest req,@RequestParam("startrow") int startrow) {
        Object temp = req.getSession().getAttribute("loginUser"); // 속성 이름 통일
        String loginUser = (temp!=null)?(String)temp:"";
        if(loginUser.equals("")) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Gson gson = new Gson();
        JsonObject json = new JsonObject();
        List<PlanDTO> list = service.myPlanList(loginUser,startrow);
        HashMap<String,List<UserDTO>> userListMap = new HashMap<>();
        for(PlanDTO plan : list){
            long plannum = plan.getPlannum();
            userListMap.put(plannum+"",service.getUserListByPlannum(plannum));
        }
        json.add("planList", gson.toJsonTree(list));
        json.add("userList", gson.toJsonTree(userListMap));
        String result = gson.toJson(json);
        return new ResponseEntity<String>(result, HttpStatus.OK);
    }
}