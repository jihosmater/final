package com.team.demo.domain.dto;

import lombok.Data;

@Data
public class ScheduleDTO {
    private long schedulenum;
    private String scheduletitle;
    private String schedulememo;
    private String starttime;
    private String endtime;
    private long plannum;
}
