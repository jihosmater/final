package com.team.demo.mapper;

import com.team.demo.domain.dto.ReplyDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplyMapper {
    int insertReply(ReplyDTO reply);
    ReplyDTO getLastReply(String userid);

//    ReplyDTO getDetail(long replynum);
    List<ReplyDTO> getList(long replynum, long placeid);

//    ArrayList<String> getReplyList(long contentId);

    List<ReplyDTO> selectReplyByContentId(long placid);
//    long getTotal(long boardnum);

//    int updateReply(ReplyDTO reply);
//
    int deleteReply(long replynum);

    int getAlready(long placeid,String userid);
//
//
//    int getRecentReplyCnt(long boardnum);
    
    int userDeleteReply(String userid);
}
