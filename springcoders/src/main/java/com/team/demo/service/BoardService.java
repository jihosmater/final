package com.team.demo.service;

import java.util.List;

import com.team.demo.domain.dto.BoardDTO;
import com.team.demo.domain.dto.Criteria;
import com.team.demo.domain.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface BoardService {

    List<BoardDTO> getList(Criteria cri);
    long getTotal(Criteria cri);
    long getLastNum(String userid);

    boolean regist(BoardDTO board, MultipartFile[] files) throws Exception;
    BoardDTO getDetail(long boardnum);
    List<FileDTO> getFiles(long boardnum);
    boolean remove(long boardnum);

    boolean boardPayment(String loginUser, long boardnum);

    boolean checkBuyState(long boardnum);

    String  getBuyState(long boardnum);
}