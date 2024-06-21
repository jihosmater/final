package com.team.demo.domain.dto;

import lombok.Data;

@Data
public class PageDTO {
    private int startPage;
    private int endPage;
    private int realEnd;
    private long total;
    private boolean prev, next;
    private Criteria cri;

    public PageDTO(long total, Criteria cri,int pageBtnCnt) {
        int pagenum = cri.getPagenum();
        this.cri = cri;
        this.total = total;

        this.endPage = (int)Math.ceil(pagenum/(pageBtnCnt*1.0))*pageBtnCnt;
        this.startPage = this.endPage - (pageBtnCnt-1);
        this.realEnd = (int)Math.ceil(total*1.0/cri.getAmount());
        this.realEnd = this.realEnd == 0 ? 1 : this.realEnd;
        this.endPage = endPage > realEnd ? realEnd : endPage;

        this.prev = this.startPage > 1;
        this.next = this.endPage < this.realEnd;
    }
}

