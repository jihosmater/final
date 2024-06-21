package com.team.demo.domain.dto;

import lombok.Data;

@Data
public class PlanDTO {
    private long plannum;
    private String plantitle;
    private String planmemo;
    private String startdate;
    private String enddate;
    private String category;

    @Override
    public String toString() {
        return "PlanDTO{" +
                "plannum=" + plannum +
                ", plantitle='" + plantitle + '\'' +
                ", planmemo='" + planmemo + '\'' +
                ", startdate='" + startdate + '\'' +
                ", enddate='" + enddate + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
