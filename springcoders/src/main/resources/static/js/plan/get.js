// 전역 변수 시작
let placePosition = new Map(); // 지도에 표시할 장소 리스트
// 전역 변수 끝

// 카카오 멥 시작
let mapContainer = document.getElementById('kakaomap');
let mapOption;
let map;
// 현재위치 위도 경도 받아오기
// HTML5의 geolocation으로 사용할 수 있는지 확인합니다
if (navigator.geolocation) {
    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
    navigator.geolocation.getCurrentPosition(function (position) {
        const latitude = position.coords.latitude; // 위도
        const longitude = position.coords.longitude; // 경도
        mapOption = {
            center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
            level: 5 // 지도의 확대 레벨
        };
        map = new kakao.maps.Map(mapContainer, mapOption);
        setMarkerImg();

    });
}else{
    console.log(2)
    mapOption = { // 지도를 표시할 div
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 5 // 지도의 확대 레벨
    };
    map = new kakao.maps.Map(mapContainer, mapOption);
    setMarkerImg();

}
let markers = [];

// 주소로 좌표 리턴  use async/await를 사용한 방법
async function getCoordinate(address) {
    var geocoder = new kakao.maps.services.Geocoder();
    return new Promise((resolve, reject) => {
        geocoder.addressSearch(address, function(result, status) {
            // 정상적으로 검색이 완료됐으면
            if (status === kakao.maps.services.Status.OK) {
                const coordinate = [result[0].y, result[0].x];
                resolve(coordinate);
            } else {
                reject(new Error("Failed to get coordinates"));
            }
        });
    });
}

function setBounds() {
    let bounds = new kakao.maps.LatLngBounds();
    const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png';
    const imageSize = new kakao.maps.Size(36, 37);
    removeMarker();
    for (let key of placePosition.keys()) {
        const imgOptions = {
            spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin: new kakao.maps.Point(0, ((key-1) * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        };
        const markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions);
        const  marker = new kakao.maps.Marker({
            image: markerImage,
            position:placePosition.get(key)
        });
        marker.setMap(map);
        markers.push(marker);
        bounds.extend(placePosition.get(key));
    }
    map.setBounds(bounds);
}
// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for (let i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
}

// 카카오 맵 끝


// 일정 날짜 수정 시작
const dayOfWeek = ["일", "월", "화", "수", "목", "금", "토"];
const planDates = document.getElementsByClassName("date");
const dates = new Array();
for(let date of planDates) {
    const YMD = date.innerText.split("_").map(Number);
    dates.push(YMD);
    const num = new Date(YMD[0] + "-" + YMD[1] + "-" + YMD[2]).getDay();
    // date.innerText = YMD[0]+"년 "+YMD[1]+"월 "+YMD[2]+"일"+"("+dayOfWeek[num]+")";
    date.innerText = YMD[1] + "월 " + YMD[2] + "일 " + dayOfWeek[num] + "요일";
}
// 일정 날짜 수정 끝

// 일정 달력 생성 시작
const celendarBox = document.getElementById("celendarBox");
let dayOfMonth = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
function setCelendar(year, month, daySet, flag) {
    const str = year + "-" + (month.length == 1 ? "0" + month : month) + "-01";// ex : '2024-05-01'
    const date = new Date(str);// str 날의 정보들 - 요일
    const daynum = date.getDay();
    let days = "<tr><th>일</th><th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th>토</th></tr>";
    days += "<tr>"
    // true이면 달력 빈 공간에도 일자 표시
    if (flag) {
        for (let i = daynum - 1; i >= 0; i--) {
            days += `<th class='day'>${dayOfMonth[(month - 1 == 0 ? 12 : day - 1)] - i}</th>`
        }
    } else { // false이면 달력 빈 공간에 일자 표시X
        for (let i = daynum - 1; i >= 0; i--) {
            days += `<th></th>`
        }
    }
    const monthCnt = dayOfMonth[month] + (((year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) && month == 2) ? 1 : 0);
    for (let i = 1; i <= monthCnt; i++) {
        if ((i + daynum) % 7 == 1) {
            if (i != 1) {
                days += "</tr><tr>";
            }
            days += `<th class='day sun ${daySet.has(i)?'select':''}'>${i}</th>`;
        } else if ((i + daynum) % 7 == 0) {
            days += `<th class='day sat ${daySet.has(i)?'select':''}'>${i}</th>`;
        } else {
            days += `<th class='day ${daySet.has(i)?'select':''}'>${i}</th>`;
        }
    }
    days += "</tr>";
    if(daynum+monthCnt<36){
        days += "<tr><th></tr>";
    }
    return days;
}
if(dates.length==1){
    const daySet = new Set();
    daySet.add(dates[0][2]);
    celendarBox.innerHTML = `<table class="celendar"><tr><th class="yearMonth" colspan="7">${dates[0][0]}년 ${dates[0][1]}월</th></tr>${setCelendar(dates[0][0],dates[0][1],daySet)}</table>`;
}else{
    let daySet = new Set();
    if(dates[0][1]===dates[1][1]){
        daySet.add(dates[0][2]);
        daySet.add(dates[1][2]);
        celendarBox.innerHTML = `<table class="celendar"><tr><th class="yearMonth" colspan="7">${dates[0][0]}년 ${dates[0][1]}월</th></tr>${setCelendar(dates[0][0],dates[0][1],daySet)}</table>`;
    }else{
        celendarBox.classList.add("large");
        daySet.add(dates[0][2]);
        celendarBox.innerHTML = `<table class="celendar"><tr><th class="yearMonth" colspan="7">${dates[0][0]}년 ${dates[0][1]}월</th></tr>${setCelendar(dates[0][0],dates[0][1],daySet)}</table>`;
        daySet = new Set();
        daySet.add(dates[1][2]);
        celendarBox.innerHTML += `<table class="celendar"><tr><th class="yearMonth" colspan="7">${dates[1][0]}년 ${dates[1][1]}월</th></tr>${setCelendar(dates[1][0],dates[1][1],daySet)}</table>`;
    }
}
// 일정 달력 생성 끝


// 일정 카테고리 수정 시작
const planCategoryBox = document.getElementById("plancategory");
const planCategory = planCategoryBox.innerText;
const planBct = planCategory.split("|bct|");
const planCt = new Map();
for(let bct of planBct){
    const list = bct.split("|ct|");
    const ctList = new Array();
    for(let i=1;i<list.length;i++){
        ctList.push(list[i]);
    }
    planCt.set(list[0],ctList);
}
planCategoryBox.innerHTML = "";
for(let bct of planCt.keys()){
    // console.log("key : "+bct);
    for(let ct of planCt.get(bct)) {
        // console.log("ct : " + ct);\
        planCategoryBox.appendChild(createPlanCt(ct));
    }
}
function createPlanCt(ct){
    const span = document.createElement("span");
    span.classList.add("planct");
    span.innerText = "#"+ct;
    return span;
}
// 일정 카테고리 수정 끝
const scheduledatelist = document.getElementsByClassName("scheduledate");
if(scheduledatelist != undefined){
    scheduledatelist[0].classList.add("select");
}

const scheduleListTag = document.getElementsByClassName("scheduleList");
if(scheduleListTag != undefined){
    scheduleListTag[0].classList.remove("disappear");
}
// 스케줄 날짜 이벤트 삽입 시작
const scheduledateList = document.getElementsByClassName("scheduledate");
for (let btn of scheduledateList){
    btn.addEventListener("click",function (){
        const date = btn.id;
        const schedulelists = document.getElementsByClassName("scheduleList");
        for(let list of schedulelists){
            if(list.classList.contains(date)){
                list.classList.remove("disappear");
            }else{
                list.classList.add("disappear");
            }
        }
        for(let span of scheduledateList){
            if(span.id===date){
                span.classList.add("select");
                span.removeAttribute("onclick");
            }else{
                span.classList.remove("select");
                span.setAttribute("onclick",'showScheduleList('+date+')');
            }
        }
        setMarkerImg();
    })
}
// 스케줄 날짜 이벤트 삽입 끝

// 스케줄 좌표 번호 위치 지정 및 맵에 좌표 설정 시작
async function setMarkerImg(){
    const markerList = document.querySelectorAll(".scheduleList:not(.disappear) .marker");
    const scheduleList = document.querySelectorAll(".scheduleList:not(.disappear) .detailInfo");
    const promises = []; // 비동기 작업을 저장할 배열

    placePosition.clear();
    for(let i = 0 ;i<markerList.length;i++){
        const marker = markerList[i];
        marker.className = "";
        marker.classList.add("marker");
        marker.classList.add("marker_"+(i+1));

        if(scheduleList[i].children.length===4){
            const addr = scheduleList[i].children[3].children[3].innerText;
            const promise = getCoordinate(addr).then(coordinate => {
                const latlng = new kakao.maps.LatLng(coordinate[0], coordinate[1]);
                // console.log(latlng);
                placePosition.set(i+1,latlng);
            }).catch(error => {
                console.error(error);
            });
            promises.push(promise);
        }
    }
    await Promise.all(promises);
    // placeCoordinate();
    if(placePosition.size === 0){
        return;
    }
    setBounds();
}
// 스케줄 좌표 번호 위치 지정 끝


