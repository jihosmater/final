package com.team.demo.domain.dto;

import lombok.Data;

@Data
public class PlaceDTO {
    private long placeid;
    private String placename;
    private String addr;
    private String phone;
    private String category;

    @Override
    public String toString() {
        return "PlaceDTO{" +
                "placeid=" + placeid +
                ", placename='" + placename + '\'' +
                ", addr='" + addr + '\'' +
                ", phone='" + phone + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
