package com.team.demo.service;

import com.team.demo.domain.dto.ReplyDTO;

import java.util.List;

public interface ReplyService {

    ReplyDTO regist(ReplyDTO reply,long placeid);

//    ReplyDTO getDetail(long replynum);
//    //목록 가져가기
//    ReplyDTO getList(long replynum, long contentid);
List<ReplyDTO> getList(long replynum, long contentId);

    List<ReplyDTO> getAllList( long contentid);
//
//    boolean modify(ReplyDTO reply);
//
    boolean remove(long replynum);
    int getAlready(long placeid,String userid);
}
