package com.team.demo.service;

import com.team.demo.domain.dto.PlaceDTO;
import com.team.demo.domain.dto.ReplyDTO;

import javax.swing.plaf.synth.Region;
import java.util.List;
import java.util.Map;

public interface RegionService {
//     List<ReplyDTO> getReplyList(long contentId);
    List<ReplyDTO> getReplyList(long placeid);
    boolean addRegion(Region region);
    boolean removeRegion(Region region);

    PlaceDTO getPlaceById(long placeid);

    boolean insertPlace(Map<String, String> commonItem);
}
