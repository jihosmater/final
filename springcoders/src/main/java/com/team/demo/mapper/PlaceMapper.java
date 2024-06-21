package com.team.demo.mapper;

import com.team.demo.domain.dto.Criteria;
import com.team.demo.domain.dto.PlaceDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlaceMapper {
	//C
	int insertPlace(PlaceDTO place);
	//R
	PlaceDTO getPlaceById(long placeid);
	//U
	int updatePlace(PlaceDTO place);
	//D
	int deletePlace(long placeid);

	List<PlaceDTO> getList(Criteria cri, String userid);

	List<Long> getLikeCntList(@Param("cri") Criteria cri, @Param("userid") String userid);

	long getListCnt(@Param("cri") Criteria cri,@Param("userid") String userid);

	int insertPR(@Param("placeid") long placeid, @Param("replynum") long replynum);

	int insertCategory(String categoryname);
	String getCategory(String categoryname);

	List<PlaceDTO> getPlaceListByPlannum(long plannum);

    List<PlaceDTO> findFavoritesByUserId(String userid);

    List<PlaceDTO> getPlaceListByArea(String area);
}