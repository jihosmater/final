package com.team.demo.service;

import com.team.demo.domain.dto.Criteria;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface ManagerService {
    HashMap<String,Integer> manageInfo();

    long getTotal(Criteria cri);

    List<Map<String, Object>> notAllowedBoardInfo(Criteria cri);

    boolean completeDeal(long boardnum);
    boolean cancelDeal(long boardnum);
}
