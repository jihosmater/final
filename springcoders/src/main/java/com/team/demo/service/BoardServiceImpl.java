package com.team.demo.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import com.team.demo.domain.dto.FileDTO;
import com.team.demo.domain.dto.UserDTO;
import com.team.demo.mapper.FileMapper;
import com.team.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team.demo.domain.dto.BoardDTO;
import com.team.demo.domain.dto.Criteria;
import com.team.demo.mapper.BoardMapper;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

@Service
public class BoardServiceImpl implements BoardService {
    @Value("${file.dir}")
    private String saveFolder;

    @Autowired
    private BoardMapper bmapper;

    @Autowired
    private FileMapper fmapper;

    @Autowired
    private UserMapper umapper;

    @Override
    public List<BoardDTO> getList(Criteria cri) {
        return bmapper.getList(cri);
    }

    @Override
    public long getTotal(Criteria cri) {
        return bmapper.getTotal(cri);
    }

    @Override
    public long getLastNum(String userid) {
        return bmapper.getLastNum(userid);
    }

    @Override
    public boolean regist(BoardDTO board, MultipartFile[] files) throws Exception {
        if(bmapper.insertBoard(board) != 1) {
            return false;
        }
        if(files == null || files.length == 0) {
            return true;
        }
        else {
            //방금 등록한 게시글 번호
            long boardnum = bmapper.getLastNum(board.getUserid());
            boolean flag = false;
            System.out.println("파일 개수 : "+files.length);

            for(int i=0;i<files.length-1;i++) {
                MultipartFile file = files[i];
                System.out.println(file.getOriginalFilename());

                //apple.png
                String orgname = file.getOriginalFilename();
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
                System.out.println("boardnum = " + boardnum);
                fdto.setFileid("board_"+boardnum);

                flag = fmapper.insertFile(fdto) == 1;

                //실제 파일 업로드
                file.transferTo(new File(path));

                if(!flag) {
                    //업로드했던 파일 삭제, 게시글 데이터 삭제, 파일 data 삭제, ...
                    return false;
                }
            }
            return true;
        }
    }
    @Override
    public boolean remove(long boardnum) {
        if(bmapper.deleteBoard(boardnum) == 1) {
            String fileid = "board_"+boardnum;
            List<FileDTO> files = fmapper.getFiles(fileid);
            for(FileDTO fdto : files) {
                File file = new File(saveFolder,fdto.getSystemname());
                if(file.exists()) {
                    file.delete();
                    fmapper.deleteFileBySystemname(fdto.getSystemname());
                }
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean boardPayment(String loginUser, long boardnum) {
        UserDTO user = umapper.getUserById(loginUser);
        int usercoin = user.getUsercoin();
        String buyId = user.getUserid();

        BoardDTO board = bmapper.getBoardByNum(boardnum);
        int price = board.getPrice();

        usercoin = usercoin - price;
        user.setUsercoin(usercoin);
        user.setUserid(buyId);
        umapper.updateUser(user);
        return bmapper.updateBuyState(buyId,boardnum)==1;
    }



    @Override
    public List<FileDTO> getFiles(long boardnum) {
        FileDTO file = new FileDTO();
        file.setFileid("board_"+boardnum);
        return fmapper.getFiles(file.getFileid());
    }

    @Override
    public BoardDTO getDetail(long boardnum) {
        return bmapper.getBoardByNum(boardnum);
    }

    @Override
    public boolean checkBuyState(long boardnum) {
        return bmapper.checkBuyState(boardnum) == 1;
    }

    @Override
    public String getBuyState(long boardnum) {
        return bmapper.getBuyState(boardnum);
    }

}