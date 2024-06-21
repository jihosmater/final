package com.team.demo.domain.dto;

import lombok.Data;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;

@Data
//페이지를 구성하는 기준
public class Criteria {
    private int pagenum;// 페이지번호
    private int amount;// 페이지 당 객체 수
    private String type;// 즐겨찾기, 명소, 식당, 카페
    private String keyword;// 검색 키워드 : 제목,카테고리
    private int startrow;//

    public Criteria() {
        this(1,10);
    }
    public Criteria(int pagenum, int amount) {
        this.pagenum = pagenum;
        this.amount = amount;
        this.startrow = (this.pagenum - 1) * this.amount;
    }

    public void setPagenum(int pagenum) {
        this.pagenum = pagenum;
        this.startrow = (this.pagenum - 1) * this.amount;
    }

    //	MyBatis에서 #{typeArr} 사용 가능
    public String[] getTypeArr() {
        return type == null ? new String[] {} : type.split("/&&/");
    }

    public String getListLink() {							// ? 앞에 붙는 URI 문자열
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
                .queryParam("pagenum", pagenum)
                .queryParam("amount", amount)
                .queryParam("keyword", keyword)
                .queryParam("type", type);

        return builder.toUriString();
    }
}








