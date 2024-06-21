package com.team.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ReplyPageDTO {
    private long replyCnt;
    private List<ReplyDTO> list;
}
