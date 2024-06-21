package com.team.demo.service;

import com.team.demo.domain.dto.Criteria;
import com.team.demo.domain.dto.FileDTO;
import com.team.demo.domain.dto.PlaceDTO;
import com.team.demo.mapper.FileMapper;
import com.team.demo.mapper.PlaceMapper;
import com.team.demo.mapper.UserMapper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PlaceServiceImpl implements PlaceService{
    @Value("${file.dir}")
    private String saveFolder;

    @Autowired
    private PlaceMapper pcmapper;
    @Autowired
    private UserMapper umapper;
    @Autowired
    private FileMapper fmapper;

    @Override
    public boolean insertPlace(PlaceDTO place) {
        // category데이터 쌓기 위해
        if(pcmapper.getCategory(place.getCategory()) == null){
            pcmapper.insertCategory(place.getCategory());
        }
        return pcmapper.insertPlace(place)==1;
    }

    @Override
    public PlaceDTO getDetail(long placeid) {
        return pcmapper.getPlaceById(placeid);
    }

    @Override
    public List<PlaceDTO> getList(Criteria cri, String userid) {
        return pcmapper.getList(cri,userid);
    }

    @Override
    public List<Long> getLikeCntList(Criteria cri, String userid) {
        return pcmapper.getLikeCntList(cri,userid);
    }

    @Override
    public long getTotal(Criteria cri,String userid) {
        return pcmapper.getListCnt(cri,userid);
    }

    @Override
    public boolean modify(PlaceDTO place) {
        return false;
    }

    @Override
    public boolean addFavoriteList(String userid, long placeid) {
        return umapper.addFavoriteList(userid,placeid);
    }

    public boolean checkFavoritePlace(String userid, long placeid){
        return umapper.checkFavoritePlace(userid,placeid) == 1;
    }


    @Override
    public boolean getImgByCrawling(PlaceDTO place) {
        String url = "http://place.map.kakao.com/" + place.getPlaceid();

        // ChromeOptions 객체 생성
        ChromeOptions options = new ChromeOptions();
        // 헤드리스 모드 활성화
        options.addArguments("--headless");
        options.addArguments("--disable-gpu"); // 필요에 따라 추가
        options.addArguments("--no-sandbox"); // 필요에 따라 추가
        options.addArguments("--disable-dev-shm-usage"); // 필요에 따라 추가
        // WebDriver 객체 생성 시 옵션 적용
        WebDriver driver = new ChromeDriver(options);
        driver.get(url);
        boolean insertFile = false;
        // 크롤링 로직 추가
        System.out.println("크롤링 시작");
        try {
            Thread.sleep(2000);
            WebElement spanElement = driver.findElement(By.className("bg_present"));
            String styleAttribute = spanElement.getCssValue("background-image");
            // URL 추출 및 형식 정리
            String imageUrl = styleAttribute.split("\"")[1]; // "url("//t1.kakaocdn.net/thumb/..." -> //t1.kakaocdn.net/thumb/...
            if (imageUrl.startsWith("//")) {
                imageUrl = "http:" + imageUrl; // http://t1.kakaocdn.net/thumb/...
            }
            System.out.println("크롤링한 이미지URL : " + imageUrl);
            // 이미지 파일 저장 경로 설정
            LocalDateTime now = LocalDateTime.now();
            String time = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
            String systemname = time+ UUID.randomUUID().toString()+ ".jpg";
            String destinationFile = saveFolder+systemname;
            // 이미지 다운로드
            URL imgUrl = new URL(imageUrl);
            try (InputStream in = imgUrl.openStream(); FileOutputStream out = new FileOutputStream(destinationFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            FileDTO file = new FileDTO();
            file.setSystemname(systemname);
            file.setOrgname(place.getPlacename()+".jpg");
            file.setFileid("place_"+place.getPlaceid());
            insertFile = fmapper.insertFile(file) == 1;
        } catch (Exception e) {
            System.out.println("PlaceServiceImpl - getImgByCrawling 에러 : "+e.getMessage());
//            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }
        return insertFile;
    }

    @Override
    public List<String> getPlcaeimgList(List<PlaceDTO> list) {
        List<String> imgList = new ArrayList<>();
        for(PlaceDTO place : list){
            FileDTO file = fmapper.getFileByFileid("place_"+place.getPlaceid());
            if(file != null){
                imgList.add("/file/thumbnail?systemname="+file.getSystemname());
            }else{
                imgList.add("/file/thumbnail?systemname=no_image.jpg");
            }
        }
        return imgList;
    }
}
