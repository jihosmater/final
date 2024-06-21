package com.team.demo.service;

import com.team.demo.domain.dto.*;
import com.team.demo.mapper.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
import java.util.*;

@Service
public class PlanServiceImpl implements PlanService{
    @Value("${file.dir}")
    private String saveFolder;

    @Autowired
    private PlanMapper pmapper;
    @Autowired
    private ScheduleMapper smapper;
    @Autowired
    private PlaceMapper placeMapper;
    @Autowired
    private FileMapper fmapper;
    @Autowired
    private PlaceServiceImpl placeServiceImpl;


    @Override
    public boolean createPlan(PlanDTO plan, String userid) {
        if(pmapper.insertPlan(plan)==1){
            plan = pmapper.getLastPlan();
            pmapper.insertUP(userid,plan.getPlannum(),"creater");
            return true;
        }
        return false;
    }

    @Override
    public PlanDTO getDetail(long plannum) {
        return pmapper.getPlanByNum(plannum);
    }

    @Override
    public List<PlanDTO> getList(String userid) {
        return List.of();
    }

    @Override
    public boolean modify(PlanDTO planDTO, List<UserDTO> joinUsers) {
        return false;
    }

    @Override
    public PlanDTO getLastPlanByUserid(String userid) {
        return pmapper.getLastPlanByUserid(userid);
    }

    @Override
    public boolean insertPP(long plannum, long placeid) {
        return pmapper.insertPP(plannum,placeid)==1;
    }

    @Override
    public boolean inviteMate(String userid,long plannum) {
        return pmapper.insertUP(userid,plannum,"mate")==1;
    }

    @Override
    public boolean insertSchedule(ScheduleDTO schedule) {
        return smapper.insertSchedule(schedule)==1;
    }

    @Override
    public UserDTO getCreater(long plannum) {
        List<UserDTO> list = pmapper.getUserList(plannum,"creater");
        if(list != null&&list.size()>0){
            return pmapper.getUserList(plannum,"creater").get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<UserDTO> getMateList(long plannum) {
        return pmapper.getUserList(plannum,"mate");
    }

    @Override
    public List<ScheduleDTO> getscheduleList(long plannum) {
        return smapper.getScheduleListByPlanNum(plannum);
    }

    @Override
    public Map<String, PlaceDTO> getPlaceMapByPlannum(long plannum) {
        List<PlaceDTO> list = placeMapper.getPlaceListByPlannum(plannum);
        Map<String, PlaceDTO> placeMap = new HashMap<>();
        for(PlaceDTO place : list){
            placeMap.put(place.getPlaceid()+"",place);
        }
        return placeMap;
    }

    @Override
    public Map<String,String> getPlaceImgMap(Map<String, PlaceDTO> placeMap) {
        Map<String,String> imgMap = new HashMap<>();
        for(PlaceDTO place : placeMap.values()){
            FileDTO file = fmapper.getFileByFileid("place_"+place.getPlaceid());
            if(file != null){
                imgMap.put(place.getPlaceid()+"","/file/thumbnail?systemname="+file.getSystemname());
            }else{
                imgMap.put(place.getPlaceid()+"","/file/thumbnail?systemname=no_image.jpg");
            }
        }
        return imgMap;
    }

    @Override
    public long countPlan(Criteria cri) {
        return pmapper.getPlanCnt(cri);
    }

    @Override
    public List<PlanDTO> getPlans(Criteria cri) {
        return pmapper.getPlans(cri);
    }

    @Override
    public List<PlaceDTO> getPlace(String area) {
        return placeMapper.getPlaceListByArea(area);
    }

    @Override
    public void addPlaces(String area) {
        String url = "https://map.kakao.com/link/search/"+area;
        String[] text = area.split(" ");
        String bct = text[text.length-1];
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
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("window.scrollBy(0, 500);"); // 세로로 500픽셀 스크롤

            System.out.println(1);
            List<WebElement> placeBox = driver.findElements(By.cssSelector(".PlaceItem.clickArea"));
            System.out.println(2);
            List<PlaceDTO> list = new ArrayList<>();
            for(int i = 0; i<(Math.min(placeBox.size(), 4)); i++) {
                System.out.println(3+"-"+i);
                WebElement tag = placeBox.get(i);
                PlaceDTO place = new PlaceDTO();
                place.setPlaceid(Long.parseLong(tag.findElement(By.className("moreview")).getAttribute("href").split("https://place.map.kakao.com/")[1]));
                place.setPlacename(tag.findElement(By.className("link_name")).getText());
                place.setAddr(tag.findElement(By.cssSelector(".addr p:nth-child(1)")).getText());
                place.setPhone(tag.findElement(By.className("phone")).getText());
                place.setCategory(bct+"/&&/"+tag.findElement(By.cssSelector(".subcategory.clickable")).getText());
                placeMapper.insertPlace(place);
                list.add(place);
            }
            for(PlaceDTO place : list){
                System.out.println(place.getPlaceid());
                getImgByCrawling(place);
            }
        } catch (Exception e) {
            System.out.println("PlaceServiceImpl - addPlaces 에러 : "+e.getMessage());
//            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }
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
            FileDTO file = new FileDTO();
            file.setSystemname("no_image.jpg");
            file.setOrgname(place.getPlacename()+".jpg");
            file.setFileid("place_"+place.getPlaceid());
            insertFile = fmapper.insertFile(file) == 1;
        } finally {
            // WebDriver 종료
            driver.quit();
        }
        return insertFile;
    }
}
