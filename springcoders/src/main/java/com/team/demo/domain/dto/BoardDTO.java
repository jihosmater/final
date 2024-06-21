package com.team.demo.domain.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;

@Data
public class BoardDTO {
    private long boardnum;
    private String boardtitle;
    private String boardcontents;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Timestamp travelPlansStart;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Timestamp travelPlansEnd;

    private Timestamp updatedate;
    private Timestamp created;
    private int price;
    private String userid;

    public Timestamp getTravelPlansStart() {
        return travelPlansStart;
    }

    public void setTravelPlansStart(String travelPlansStart) {
        this.travelPlansStart =  Timestamp.valueOf(travelPlansStart);
    }

    public Timestamp getTravelPlansEnd() {
        return travelPlansEnd;
    }

    public void setTravelPlansEnd(String travelPlansEnd) {
        this.travelPlansEnd = Timestamp.valueOf(travelPlansEnd);
    }
}