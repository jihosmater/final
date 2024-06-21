package com.team.demo.mapper;

import com.team.demo.domain.dto.BoardDTO;
import com.team.demo.domain.dto.Criteria;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
   //insert
   int insertBoard(BoardDTO board);

   List<BoardDTO> getList(Criteria cri);
   long getTotal(Criteria cri);
   long getLastNum(String userid);
   BoardDTO getBoardByNum(long boardnum);

   int updateBoard(BoardDTO board);

   //@Param : MyBatis에 일반 변수로 두 개 이상을 넘길 때, XML쪽에서 사용하기 위해 @Param 어노테이션을 이용해서 name을 달아준다.
   int updateReadCount(@Param("boardnum")long boardnum,@Param("readcount") int readcount);

   int deleteBoard(long boardnum);
   
   int UserDelete(String userid);
   int updateBuyState(String userid,long boardnum);

   int checkBuyState(long boardnum);

    String getBuyState(long boardnum);

//   관리자페이지 홈 데이터 시작
    int boardCnt();
    int completedCnt();
    int requestCnt();

    List<Map<String, Object>> notAllowedBoardInfo(Criteria cri);

    boolean completeDeal(long boardnum);
    boolean cancelDeal(long boardnum);
}