package com.team.demo.service;

import com.team.demo.domain.dto.ReplyDTO;
import com.team.demo.mapper.PlaceMapper;
import com.team.demo.mapper.ReplyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyMapper rmapper;
    @Autowired
    private PlaceMapper pmapper;

    @Override
    public ReplyDTO regist(ReplyDTO reply,long placeId) {
        //등록 데이터 로직
        if(rmapper.insertReply(reply) == 1) {
            ReplyDTO temp= rmapper.getLastReply(reply.getUserid());
            long replynum = temp.getReplynum();
            if(pmapper.insertPR(placeId,replynum)==1){
                return temp;
            }
            else {
//                관계테이블 삽입 실패 시 댓글 삭제후 null return
                return null;
            }
        }
        return null;
    }

//    @Override
//    public ReplyDTO getDetail(long replynum) {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
@Override
public List<ReplyDTO> getList(long replynum, long contentId) {
    return rmapper.getList(replynum, contentId);
}


@Override
public boolean remove(long replynum) {return rmapper.deleteReply(replynum) == 1;}

    @Override
    public int getAlready(long placeid,String userid) {
        return rmapper.getAlready(placeid,userid);
    }

    @Override
    public List<ReplyDTO> getAllList( long contentid) {
        return rmapper.selectReplyByContentId( contentid);
    }
//
//    @Override
//    public boolean modify(ReplyDTO reply) {
//        return rmapper.updateReply(reply) == 1;
//    }
//
//    @Override
//    public boolean remove(long replynum) {
//        return rmapper.deleteReply(replynum) == 1;
//    }

}
