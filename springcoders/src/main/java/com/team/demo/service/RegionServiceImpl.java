package com.team.demo.service;

import com.team.demo.domain.dto.FileDTO;
import com.team.demo.domain.dto.PlaceDTO;
import com.team.demo.domain.dto.ReplyDTO;
import com.team.demo.mapper.FileMapper;
import com.team.demo.mapper.PlaceMapper;
import com.team.demo.mapper.ReplyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.Region;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RegionServiceImpl implements RegionService {
    @Value("${file.dir}")
    private String saveFolder;
    private static final Logger log = LoggerFactory.getLogger(RegionServiceImpl.class);
    @Autowired
    private ReplyMapper rmapper;

    @Autowired
    private PlaceMapper pmapper;

    @Autowired
    private FileMapper fmapper;

    @Override
    public List<ReplyDTO> getReplyList(long placeid) {
        return rmapper.selectReplyByContentId(placeid);
    }

    @Override
    public boolean addRegion(Region region) {
        return false;
    }

    @Override
    public boolean removeRegion(Region region) {
        return false;
    }

    @Override
    public PlaceDTO getPlaceById(long placeid) {
        return pmapper.getPlaceById(placeid);
    }

    @Override
    public boolean insertPlace(Map<String, String> commonItem) {
        PlaceDTO place = new PlaceDTO();
        long placeid = Long.parseLong("1000"+commonItem.get("contentid"));
        place.setPlaceid(placeid);

        place.setAddr(commonItem.get("addr1"));
        place.setPlacename(commonItem.get("title"));
        if(commonItem.get("contenttypeid").equals("12")) {
            place.setCategory("명소");
        }else if(commonItem.get("contenttypeid").equals("39")){
            place.setCategory("식당");
        }
        else{
            place.setCategory("기타");
        }

        if(commonItem.get("tel") !=null && !commonItem.get("tel").isEmpty()) {
                place.setPhone(commonItem.get("tel"));
        }
        else{
            place.setPhone(null);
        }


        System.out.println("place = " + place);
        pmapper.insertPlace(place);
        // 이미지 파일 저장 경로 설정
        LocalDateTime now = LocalDateTime.now();
        String time = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String systemname = time+ UUID.randomUUID() + ".jpg";
        String destinationFile = saveFolder+systemname;
        String imageUrl = (commonItem.get("firstimage")==null)?"":commonItem.get("firstimage");
        // 이미지 다운로드
        URL imgUrl = null;
        try {
            imgUrl = new URL(imageUrl);
            try (InputStream in = imgUrl.openStream(); FileOutputStream out = new FileOutputStream(destinationFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        FileDTO file = new FileDTO();
        file.setSystemname(systemname);
        file.setOrgname(place.getPlacename()+".jpg");
        file.setFileid("place_"+place.getPlaceid());
        fmapper.insertFile(file);

        System.out.println("place = " + place);
        return false;
    }
}
