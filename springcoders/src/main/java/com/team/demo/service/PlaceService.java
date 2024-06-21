package com.team.demo.service;

import com.team.demo.domain.dto.Criteria;
import com.team.demo.domain.dto.PlaceDTO;

import java.util.List;
import java.util.Map;

public interface PlaceService {
	boolean insertPlace(PlaceDTO place);
	PlaceDTO getDetail(long placeid);
	List<PlaceDTO> getList(Criteria cri,String userid);
	boolean modify(PlaceDTO place);
	boolean addFavoriteList(String userid, long placeid);
	boolean checkFavoritePlace(String userid, long placeid);
	Object getLikeCntList(Criteria cri,String userid);
	long getTotal(Criteria cri,String userid);

	boolean getImgByCrawling(PlaceDTO place);

	List<String> getPlcaeimgList(List<PlaceDTO> list);
}
