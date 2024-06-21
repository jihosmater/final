package com.team.demo.mapper;

import com.team.demo.domain.dto.Criteria;
import com.team.demo.domain.dto.PlanDTO;
import com.team.demo.domain.dto.UserDTO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlanMapper {
	//C
	int insertPlan(PlanDTO plan);
	//R
	PlanDTO getPlanByNum(long plannum);
	//U
	int updatePlan(PlanDTO plan);
	//D
	int deletePlan(long plannum);

	PlanDTO getLastPlan();
	int insertUP(@Param("userid") String userid,@Param("plannum") long plannum,@Param("part") String part);
	PlanDTO getLastPlanByUserid(String userid);
	int insertPP(@Param("plannum") long plannum,@Param("placeid") long placeid);

	List<UserDTO> getUserList(@Param("plannum") long plannum , @Param("part") String part);
	long getPlanCnt(Criteria cri);
	List<PlanDTO> getPlans(Criteria cri);

    List<PlanDTO> getPlanListByUserid(@Param("userid") String userid,@Param("startrow") int startrow);
}
