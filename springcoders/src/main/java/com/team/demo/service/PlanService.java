package com.team.demo.service;


import com.team.demo.domain.dto.*;

import java.util.List;
import java.util.Map;

public interface PlanService {
	boolean createPlan(PlanDTO planDTO,String userid);
	PlanDTO getDetail(long plannum);
	List<PlanDTO> getList(String userid);
	boolean modify(PlanDTO planDTO, List<UserDTO> joinUsers);

	PlanDTO getLastPlanByUserid(String userid);
	boolean insertPP(long plannum, long placeid);
	boolean inviteMate(String userid,long plannum);

	boolean insertSchedule(ScheduleDTO schedule);
	UserDTO getCreater(long plannum);
	List<UserDTO> getMateList(long plannum);
	List<ScheduleDTO> getscheduleList(long plannum);
	Map<String, PlaceDTO> getPlaceMapByPlannum(long plannum);
	Map<String,String> getPlaceImgMap(Map<String, PlaceDTO> placeMap);

	long countPlan(Criteria cri);
	List<PlanDTO> getPlans(Criteria cri);

    List<PlaceDTO> getPlace(String area);

	void addPlaces(String area);

	boolean getImgByCrawling(PlaceDTO place);
}
