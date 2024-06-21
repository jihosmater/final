package com.team.demo.mapper;

import com.team.demo.domain.dto.ScheduleDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleMapper {
    //C
    int insertSchedule(ScheduleDTO schedule);
    //R
    ScheduleDTO getScheduleByNum(long schedulenum);
    //U
    int updateSchedule(ScheduleDTO schedule);
    //D
    int deleteSchedule(long schedulenum);

    List<ScheduleDTO> getScheduleListByPlanNum(long plannum);
}
