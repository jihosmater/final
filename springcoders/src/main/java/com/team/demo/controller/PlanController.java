package com.team.demo.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.team.demo.domain.dto.*;
import org.springframework.ui.Model;

import com.team.demo.service.ChatService;
import com.team.demo.service.PlanService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Controller
@RequestMapping("/plan/*")
public class PlanController {
    @Autowired
    private PlanService service;
    
    @Autowired
    private ChatService chatservice;

    @GetMapping("create")
    public void create(Model model, HttpServletRequest req) {
        System.out.println("/plane/create : GET");
        // 임의로 로그인함
        HttpSession session = req.getSession();
        String loginUser = session.getAttribute("loginUser")==null?"":(String) session.getAttribute("loginUser");
        if(req.getAttribute("messagae")!=null){
            String message = (String) req.getAttribute("messagae");
            model.addAttribute("message",message);
        }
    }

    @PostMapping(value="create",produces = "application/json;charset=utf-8")
    public String create(PlanDTO plan, @RequestParam(required = false) List<Long> placeidList, @RequestParam(required = false) List<String> useridList,
                         @RequestParam(required = false) List<Integer> scheduleCnt, @RequestParam(required = false) List<String> scheduletitle,
                         @RequestParam(required = false) List<String> schedulememo, @RequestParam(required = false) List<String> schedulestarttime,
                         @RequestParam(required = false) List<String> scheduleendtime , Model model, HttpServletRequest req) {
        System.out.println("/plane/create : POST");
        HttpSession session = req.getSession();
        String loginUser = session.getAttribute("loginUser")==null?"":(String) session.getAttribute("loginUser");
        if(loginUser.equals("")){
            return "redirect:/user/join?order=notlogin";
        }
        // 로그인 유저가 일정 생성
        if(service.createPlan(plan,loginUser)){ // 성공
            plan = service.getLastPlanByUserid(loginUser);
            chatservice.chatRoomInsert(plan.getPlantitle(), loginUser);
            int roomno = chatservice.lastInsertRoomId();
            chatservice.chatUserInsert(roomno, plan.getPlantitle(), loginUser);

            if(placeidList!=null){
                // 일정별 들릴 장소 추가
                for(long placeid : placeidList){
                    service.insertPP(plan.getPlannum(),placeid);
                }
            }

            if(useridList!=null){
                // 여행메이트 초대
                for(String userid : useridList){
                    service.inviteMate(userid,plan.getPlannum());
                    chatservice.chatUserInsert(roomno, plan.getPlantitle(), userid);
                }
            }

            if(scheduletitle!=null){
                // 스케줄 생성후 삽입
                for(int i=0;i<scheduletitle.size();i++){
                    ScheduleDTO schedule = new ScheduleDTO();
                    schedule.setScheduletitle(scheduletitle.get(i));
                    schedule.setSchedulememo(schedulememo.get(i));
                    schedule.setStarttime(schedulestarttime.get(i));
                    schedule.setEndtime(scheduleendtime.get(i));
                    schedule.setPlannum(plan.getPlannum());
                    service.insertSchedule(schedule);
                }
            }

            return "redirect:/plan/get?plannum="+plan.getPlannum();
        }else{ // 실패
            return "redirect:/plan/create?messagae=createError";
        }
    }

    @GetMapping(value = "get",produces = "application/json;charset=utf-8")
    public void get(Model model, HttpServletRequest req) {
        System.out.println("/plane/get : GET");
        long plannum = Long.parseLong(req.getParameter("plannum"));

        // 필수
        model.addAttribute("plan",service.getDetail(plannum));
        model.addAttribute("creater",service.getCreater(plannum));
        
        // 선택
        model.addAttribute("mateList",service.getMateList(plannum));
        List<ScheduleDTO> scheduleList = service.getscheduleList(plannum);
        List<String> dates = new ArrayList<>();
        Map<String,List<ScheduleDTO>> scheduleMap = new HashMap<>();
        if(scheduleList != null&&scheduleList.size()>0){
            for(ScheduleDTO schedule : scheduleList){
                String date = schedule.getStarttime().substring(0,10);
                if(!scheduleMap.containsKey(date)){
                    dates.add(date);
                    scheduleMap.put(date,new ArrayList<ScheduleDTO>());
                }
                scheduleMap.get(date).add(schedule);
            }
        }
        Collections.sort(dates);
        model.addAttribute("dates",dates);
        model.addAttribute("scheduleMap",scheduleMap);
        Map<String,PlaceDTO> placeMap =  service.getPlaceMapByPlannum(plannum);
        model.addAttribute("placeMap",placeMap);
        model.addAttribute("placeImgMap",service.getPlaceImgMap(placeMap));
    }

    @GetMapping(value = "list",produces = "application/json;charset=utf-8")
    public void list(Model model, HttpServletRequest req, Criteria cri) {
        System.out.println("/plane/list : GET");
//        System.out.println(cri);
        cri.setAmount(8);
        long total = service.countPlan(cri);
        PageDTO page = new PageDTO(total,cri,5);

        Gson gson = new Gson();
        JsonObject json = new JsonObject();

        List<PlanDTO> planList = service.getPlans(cri);
        Map<String,List<UserDTO>> userListByPlannum = new HashMap<>();
        for(PlanDTO plan : planList){
            long plannum = plan.getPlannum();
            userListByPlannum.put(plannum+"",service.getMateList(plannum));
            userListByPlannum.get(plannum+"").add(service.getCreater(plannum));
        }
        model.addAttribute("planList",planList);
        model.addAttribute("userListByPlannum",userListByPlannum);
        model.addAttribute("pageMaker",page);
//        System.out.println("planList : "+planList.size());
//        System.out.println("userListByPlannum : "+userListByPlannum.size());
//        System.out.println("pageMaker : "+page);
    }

    @GetMapping(value = "dummy")
    public void dummy(Model model, HttpServletRequest req) {
        System.out.println("/plan/dummy : GET - 일정 더미 생성");
//        String[] area = {"서울특별시","부산광역시","대구광역시","인천광역시","광주광역시","대전광역시","울산광역시","세종특별자치시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주특별자치도"};
        String[] area = {"서울특별시","부산광역시","인천광역시"};
//        String[][] subarea = {{"종로구","중구","용산구","성동구","광진구","동대문구","중랑구","성북구","강북구","도봉구","노원구","은평구","서대문구","마포구","양천구","강서구","구로구","금천구","영등포구","동작구","관악구","서초구","강남구","송파구","강동구"},{"중구","서구","동구","영도구","부산진구","동래구","남구","북구","해운대구","사하구","금정구","강서구","연제구","수영구","사상구","기장군"},{"중구","동구","서구","남구","북구","수성구","달서구","달성군"},{"중구","동구","미추홀구","연수구","남동구","부평구","계양구","서구","강화군","옹진군"},{"동구","서구","남구","북구","광산구"},{"동구","중구","서구","유성구","대덕구"},{"중구","남구","동구","북구","울주군"},{"세종특별자치시"},{"수원시","성남시","의정부시","안양시","부천시","광명시","평택시","동두천시","안산시","고양시","과천시","구리시","남양주시","오산시","시흥시","군포시","의왕시","하남시","용인시","파주시","이천시","안성시","김포시","화성시","광주시","양주시","포천시","여주시","연천군","가평군","양평군"},{"춘천시","원주시","강릉시","동해시","태백시","속초시","삼척시","홍천군","횡성군","영월군","평창군","정선군","철원군","화천군","양구군","인제군","고성군","양양군"},{"청주시","충주시","제천시","보은군","옥천군","영동군","증평군","진천군","괴산군","음성군","단양군"},{"천안시","공주시","보령시","아산시","서산시","논산시","계룡시","당진시","금산군","부여군","서천군","청양군","홍성군","예산군","태안군"},{"전주시","군산시","익산시","정읍시","남원시","김제시","완주군","진안군","무주군","장수군","임실군","순창군","고창군","부안군"},{"목포시","여수시","순천시","나주시","광양시","담양군","곡성군","구례군","고흥군","보성군","화순군","장흥군","강진군","해남군","영암군","무안군","함평군","영광군","장성군","완도군","진도군","신안군"},{"포항시","경주시","김천시","안동시","구미시","영주시","영천시","상주시","문경시","경산시","군위군","의성군","청송군","영양군","영덕군","청도군","고령군","성주군","칠곡군","예천군","봉화군","울진군","울릉군"},{"창원시","진주시","통영시","사천시","김해시","밀양시","거제시","양산시","의령군","함안군","창녕군","고성군","남해군","하동군","산청군","함양군","거창군","합천군"},{"제주시","서귀포시"}};
        String[][] subarea = {{"양천구","강서구"},
                {"사하구","금정구","연제구"},
                {"서구"}};
//        String[] titlearea = {"서울","부산","대구","인천","광주","대전","울산","세종","경기도","강원도","충북","충남","전북","전남","경북","경남","제주도"};
        String[] titlearea = {"서울","부산","인천"};
        String[] subTitles = {"힐링 여행", "맛집 탐방", "문화유산 투어", "자연 경관 감상", "역사 탐방","해변 산책", "트레킹 여행", "박물관 방문", "전통 시장 투어", "온천 여행","테마파크 방문", "미술관 관람", "지역 축제 참여", "야경 감상", "드라이브 코스","수상 레포츠", "농촌 체험"};
        String[] scheduleContents = {"힐링 카페에서 커피 한잔","도심 속 산책로 걷기","유명 맛집에서 점심 식사","영화관에서 최신 영화 감상","도서관에서 독서와 공부","박물관 전시회 관람","야외 공연장에서 음악 감상","테마파크 놀이기구 타기","전통 시장에서 쇼핑","근교 드라이브 코스 탐방","온천에서 힐링 스파 체험","미술관에서 그림 감상","지역 축제 참여하기","야경이 아름다운 장소 방문","스포츠 경기 관람","자전거로 강변 라이딩","현지 음식 요리 교실 참여","트레킹 코스 도전하기","농장에서 농촌 체험","해변에서 해수욕 즐기기"};
        String[] userlist = {"apple","banana","cherry","melon","podo"};
        int areanum = (int)(Math.random()*area.length);
        int num1 = (int)(Math.random()*area.length);
        int subareanum = (int)(Math.random()*subarea[areanum].length);
        String title = titlearea[areanum]+" "+subTitles[num1];
        int schedulenum = (int)(Math.random()*scheduleContents.length);
        String memo = scheduleContents[schedulenum];
        String category = "지역|ct|"+area[areanum]+" "+subarea[areanum][subareanum];
        Object temp = req.getSession().getAttribute("loginUser");
        String loginUser = (String)temp;
        int selectUserCnt = (int)(Math.random()*userlist.length);
        selectUserCnt = (selectUserCnt==userlist.length)?selectUserCnt-1:selectUserCnt;
        ArrayList<String> users = new ArrayList<>();
        for(int i =0;i<selectUserCnt;i++){
            if(loginUser.equals(userlist[i])){
                selectUserCnt++;
                continue;
            }else{
                users.add(userlist[i]);
            }
        }
        int month = (int)(Math.random()*12)+1;
        int day = (int)(Math.random()*25)+1;
        int plus = (int)(Math.random()*6);
        String start = "2024_"+(month<10?"0"+month:month)+"_"+(day<10?"0"+day:day);
        day += plus;
        String end = "2024_"+(month<10?"0"+month:month)+"_"+(day<10?"0"+day:day);
        List<PlaceDTO> places = service.getPlace(subarea[areanum][subareanum]);
        if(places == null||places.size()==0){
            service.addPlaces(area[areanum]+" "+subarea[areanum][subareanum]+" 식당");
            service.addPlaces(area[areanum]+" "+subarea[areanum][subareanum]+" 명소");
            service.addPlaces(area[areanum]+" "+subarea[areanum][subareanum]+" 카페");
        }
        places = service.getPlace(subarea[areanum][subareanum]);
        ArrayList<Long> placeList = new ArrayList<>();
        int count = Math.min((int)(Math.random()*places.size()),5);
        for(int i =0;i<count;i++){
            placeList.add(places.get(i).getPlaceid());
        }
        List<ScheduleDTO> schedules = new ArrayList<>();
        for(int i =day-plus;i<=day;i++){
            String date = "2024_"+(month<10?"0"+month:month)+"_"+(i<10?"0"+i:i);
            int ct = (int)(Math.random()*4);
            for(int j =0;j<ct;j++){
                int random = (int)(Math.random()*placeList.size());
                ScheduleDTO schedule = new ScheduleDTO();
                int x = (int)(Math.random()*subTitles.length);
                int y = (int)(Math.random()*scheduleContents.length);
                schedule.setScheduletitle(subTitles[x]);
                schedule.setSchedulememo(scheduleContents[y]+"/&&/"+placeList.get(random));
                int hour = (int)(Math.random()*19);
                int min = (int)(Math.random()*60);
                schedule.setStarttime(date+"_"+(hour<10?"0"+hour:hour)+":"+(min<10?"0"+min:min));
                hour += (int)(Math.random()*5);
                schedule.setEndtime(date+"_"+(hour<10?"0"+hour:hour)+":"+(min<10?"0"+min:min));
                schedules.add(schedule);
            }
        }

        model.addAttribute("title", title);
        model.addAttribute("memo", memo);
        model.addAttribute("category", category);
        model.addAttribute("userlist",users);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("places", placeList);
        model.addAttribute("schedules",schedules);
    }
}

