package com.team.demo.service;


import com.team.demo.domain.dto.Criteria;
import com.team.demo.mapper.BoardMapper;

import com.team.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ManagerServiceImpl implements ManagerService{

    @Autowired
    private BoardMapper bmapper;

    @Autowired
    private UserMapper umapper;

    @Override
    public HashMap<String, Integer> manageInfo() {
        HashMap<String, Integer> manageMap = new HashMap<>();
        // 전체 상품 수
        int boardCnt = bmapper.boardCnt();
        System.out.println("boardCnt = " + boardCnt);
        manageMap.put("boardCnt", boardCnt);

        // 회원 수
        int userCnt = umapper.userCnt();
        System.out.println("userCnt = " + userCnt);
        manageMap.put("userCnt", userCnt);

        // 거래 완료 수 (buystate = O)
        int completedCnt = bmapper.completedCnt();
        System.out.println("completedCnt = " + completedCnt);
        manageMap.put("completedCnt", completedCnt);

        // 승인 요청 수 (buystate = X)
        int requestCnt = bmapper.requestCnt();
        System.out.println("requestCnt = " + requestCnt);
        manageMap.put("requestCnt", requestCnt);

        return manageMap;
    }

    @Override
    public long getTotal(Criteria cri) {return bmapper.getTotal(cri);}

    @Override
    public List<Map<String, Object>> notAllowedBoardInfo(Criteria cri) {return bmapper.notAllowedBoardInfo(cri);}

    @Override
    public boolean completeDeal(long boardnum) {
        return bmapper.completeDeal(boardnum);
    }

    @Override
    public boolean cancelDeal(long boardnum) {
        return bmapper.cancelDeal(boardnum);
    }


}
