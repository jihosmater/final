window.onload = function (){
    getPlaceList(1);
}
const categorys = document.querySelectorAll("#categorySelect .category");
for(let i=0;i<categorys.length;i++){
    categorys[i].addEventListener("click",function (e){
        e.preventDefault();
        const category = e.target;
        for(let c of categorys){
            c.classList.remove("select");
        }
        category.classList.add("select");
        getPlaceList(1); //ajax로 카테고리에 해당되는 리스트 가져오기 처리
    })
}

// 엔터시 검색 이벤트 추가
document.getElementById("searchPlace").addEventListener("keypress",function (e){
    if(e.code == "Enter"){
        getPlaceList(1);
    }
})
document.getElementById("seach").addEventListener("keypress",function (e){
    if(e.code == "Enter"){
        searchPlaces();
    }
})

let planmapContainer = document.getElementById('planMap'); // 지도를 표시할 div
let planmapOption = {
    center: new kakao.maps.LatLng(37.498004414546934, 127.02770621963765), // 지도의 중심좌표
    level: 5 // 지도의 확대 레벨
};
// 마커를 담을 배열입니다
let planmarkers = [];

let planmap = new kakao.maps.Map(planmapContainer, planmapOption); // 지도를 생성합니다

// 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
// 0528
// let bounds = new kakao.maps.LatLngBounds();


// 카페 마커가 표시될 좌표 배열입니다
let cafePositions = [];

// 식당 마커가 표시될 좌표 배열입니다
let restaurantPositions = []

// 숙소 마커가 표시될 좌표 배열입니다
let lodgingPositions = []

// 명소 마커가 표시될 좌표 배열
let tractionPositions = []

let totalPositions = []

var markerImageSrc = '/image/category_img.png';  // 마커이미지의 주소입니다. 스프라이트 이미지 입니다
let totalMarkers = [];
let tractionMarkers = [];
let cafeMarkers = []; // 카페 마커 객체를 가지고 있을 배열입니다
let restaurantMarkers = []; //  마커 객체를 가지고 있을 배열입니다
let lodgingMarkers = []; // 숙소 마커 객체를 가지고 있을 배열입니다

let planMapbounds;

createCafeMarkers(); // 카페 마커를 생성하고 카페 마커 배열에 추가합니다
createRestaurantMarkers(); // 식당 마커를 생성하고 식당 마커 배열에 추가합니다
createLodgingMarkers(); // 숙소 마커를 생성하고 숙소 마커 배열에 추가합니다

// changeMarker('total'); // 지도에 카페 마커가 보이도록 설정합니다


// 마커이미지의 주소와, 크기, 옵션으로 마커 이미지를 생성하여 리턴하는 함수입니다
function createMarkerImage(src, size, options) {
    var markerImage = new kakao.maps.MarkerImage(src, size, options);
    return markerImage;
}

// 좌표와 마커이미지를 받아 마커를 생성하여 리턴하는 함수입니다
function createMarker(position, image) {
    var marker = new kakao.maps.Marker({
        position: position,
        image: image
    });

    return marker;
}

function createTotalMarkers() {
    for (var i = 0; i < totalPositions.length; i++) {

        let imageSize = new kakao.maps.Size(44, 52);
        let imageOptions = {
            spriteOrigin: new kakao.maps.Point(10, 248),
            spriteSize: new kakao.maps.Size(64, 300)
        };

        // 마커이미지와 마커를 생성합니다
        var markerImage = createMarkerImage(markerImageSrc, imageSize, imageOptions),
            marker = createMarker(totalPositions[i], markerImage);

        // 생성된 마커를 카페 마커 배열에 추가합니다
        totalMarkers.push(marker);
    }
}

function setTotalMarkers(selectMap) {
    for (var i = 0; i < totalMarkers.length; i++) {
        // console.log(totalMarkers[i]);
        totalMarkers[i].setMap(selectMap);
    }
    if (selectMap == null || totalMarkers.length == 0) {
        return;
    }
    planMapbounds = new kakao.maps.LatLngBounds();
    for (var i = 0; i < totalMarkers.length; i++) {
        planMapbounds.extend(totalPositions[i]);
    }
    // selectMap.setBounds(planMapbounds);
}

function createTractionMarkers() {
    for (var i = 0; i < tractionPositions.length; i++) {

        let imageSize = new kakao.maps.Size(44, 52);
        let imageOptions = {
            spriteOrigin: new kakao.maps.Point(10, 186),
            spriteSize: new kakao.maps.Size(64, 300)
        };

        // 마커이미지와 마커를 생성합니다
        var markerImage = createMarkerImage(markerImageSrc, imageSize, imageOptions),
            marker = createMarker(tractionPositions[i], markerImage);

        // 생성된 마커를 카페 마커 배열에 추가합니다
        tractionMarkers.push(marker);
    }
}

function setTractionMarkers(selectMap) {
    for (var i = 0; i < tractionMarkers.length; i++) {
        tractionMarkers[i].setMap(selectMap);
    }
    if(selectMap == null||tractionMarkers.length==0){
        return;
    }
    planMapbounds = new kakao.maps.LatLngBounds();
    for (var i = 0; i < tractionMarkers.length; i++) {
        planMapbounds.extend(tractionPositions[i]);
    }
}


// 카페 마커를 생성하고 카페 마커 배열에 추가하는 함수입니다
function createCafeMarkers() {
    for (var i = 0; i < cafePositions.length; i++) {

        let imageSize = new kakao.maps.Size(44, 52);
        let imageOptions = {
            spriteOrigin: new kakao.maps.Point(10, 0),
            spriteSize: new kakao.maps.Size(64, 300)
        };

        // 마커이미지와 마커를 생성합니다
        var markerImage = createMarkerImage(markerImageSrc, imageSize, imageOptions),
            marker = createMarker(cafePositions[i], markerImage);

        // 생성된 마커를 카페 마커 배열에 추가합니다
        cafeMarkers.push(marker);
    }
}

// 카페 마커들의 지도 표시 여부를 설정하는 함수입니다
function setCafeMarkers(selectMap) {
    for (var i = 0; i < cafeMarkers.length; i++) {
        cafeMarkers[i].setMap(selectMap);
    }
    if(selectMap == null||cafeMarkers.length == 0){
        return;
    }
    planMapbounds = new kakao.maps.LatLngBounds();
    for (var i = 0; i < cafeMarkers.length; i++) {
        planMapbounds.extend(cafePositions[i]);
    }
}


// 식당 마커를 생성하고 식당 마커 배열에 추가하는 함수입니다
function createRestaurantMarkers() {
    for (var i = 0; i < restaurantPositions.length; i++) {

        let imageSize = new kakao.maps.Size(44, 52);
        let imageOptions = {
            spriteOrigin: new kakao.maps.Point(10, 62),
            spriteSize: new kakao.maps.Size(64, 300)
        };

        // 마커이미지와 마커를 생성합니다
        var markerImage = createMarkerImage(markerImageSrc, imageSize, imageOptions),
            marker = createMarker(restaurantPositions[i], markerImage);
        // console.log(restaurantPositions[i]);

        // 생성된 마커를 식당 마커 배열에 추가합니다
        restaurantMarkers.push(marker);
    }
}

// 식당 마커들의 지도 표시 여부를 설정하는 함수입니다
function setRestaurantMarkers(selectMap) {
    for (var i = 0; i < restaurantMarkers.length; i++) {
        restaurantMarkers[i].setMap(selectMap);
    }
    if(selectMap == null||restaurantMarkers.length == 0){
        return;
    }
    planMapbounds = new kakao.maps.LatLngBounds();
    for (var i = 0; i < restaurantMarkers.length; i++) {
        restaurantMarkers[i].setMap(selectMap);
        planMapbounds.extend(restaurantPositions[i]);
    }
}


// 숙소 마커를 생성하고 숙소 마커 배열에 추가하는 함수입니다
function createLodgingMarkers() {
    for (var i = 0; i < lodgingPositions.length; i++) {

        let imageSize = new kakao.maps.Size(44, 52);
        let imageOptions = {
            spriteOrigin: new kakao.maps.Point(10, 124),
            spriteSize: new kakao.maps.Size(64, 300)
        };

        // 마커이미지와 마커를 생성합니다
        var markerImage = createMarkerImage(markerImageSrc, imageSize, imageOptions),
            marker = createMarker(lodgingPositions[i], markerImage);

        // 생성된 마커를 숙소 마커 배열에 추가합니다
        lodgingMarkers.push(marker);
    }
}

// 숙소 마커들의 지도 표시 여부를 설정하는 함수입니다
function setLodgingMarkers(selectMap) {
    for (var i = 0; i < lodgingMarkers.length; i++) {
        lodgingMarkers[i].setMap(selectMap);
    }
    if(selectMap == null||lodgingMarkers.length == 0){
        return;
    }
    planMapbounds = new kakao.maps.LatLngBounds();
    for (var i = 0; i < lodgingMarkers.length; i++) {
        planMapbounds.extend(lodgingPositions[i]);
    }
}


// 카테고리를 클릭했을 때 type에 따라 카테고리의 스타일과 지도에 표시되는 마커를 변경합니다
function changeMarker(type) {
    let totalMenu = document.getElementById('totalMenu');
    let attractionMenu = document.getElementById('attractionMenu');
    let cafeMenu = document.getElementById('cafeMenu');
    let restaurantMenu = document.getElementById('restaurantMenu');
    let lodgingMenu = document.getElementById('lodgingMenu');

    planmap = new kakao.maps.Map(planmapContainer, planmapOption);
    if(type === 'total'){
        totalMenu.className = 'menu_selected';
        attractionMenu.className = '';
        cafeMenu.className = '';
        restaurantMenu.className = '';
        lodgingMenu.className = '';

        setTotalMarkers(planmap);
        setTractionMarkers(null);
        setCafeMarkers(null);
        setRestaurantMarkers(null);
        setLodgingMarkers(null);
    }else if(type === 'attraction'){
        totalMenu.className = '';
        attractionMenu.className = 'menu_selected';
        cafeMenu.className = '';
        restaurantMenu.className = '';
        lodgingMenu.className = '';

        setTotalMarkers(null);
        setTractionMarkers(planmap);
        setCafeMarkers(null);
        setRestaurantMarkers(null);
        setLodgingMarkers(null);
    }else if (type === 'cafe') {// 카페 카테고리가 클릭됐을 때
        totalMenu.className = '';
        attractionMenu.className = '';
        cafeMenu.className = 'menu_selected';
        restaurantMenu.className = '';
        lodgingMenu.className = '';

        // 카페 마커들만 지도에 표시하도록 설정합니다
        setTotalMarkers(null);
        setTractionMarkers(null);
        setCafeMarkers(planmap);
        setRestaurantMarkers(null);
        setLodgingMarkers(null);

    } else if (type === 'restaurant') { // 식당 카테고리가 클릭됐을 때
        // 식당 카테고리를 선택된 스타일로 변경하고
        totalMenu.className = '';
        attractionMenu.className = '';
        cafeMenu.className = '';
        restaurantMenu.className = 'menu_selected';
        lodgingMenu.className = '';

        // 식당 마커들만 지도에 표시하도록 설정합니다
        setTotalMarkers(null);
        setTractionMarkers(null);
        setCafeMarkers(null);
        setRestaurantMarkers(planmap);
        setLodgingMarkers(null);

    } else if (type === 'lodging') { // 숙소 카테고리가 클릭됐을 때
        // 숙소 카테고리를 선택된 스타일로 변경하고
        totalMenu.className = '';
        attractionMenu.className = '';
        cafeMenu.className = '';
        restaurantMenu.className = '';
        lodgingMenu.className = 'menu_selected';

        // 숙소 마커들만 지도에 표시하도록 설정합니다
        setTotalMarkers(null);
        setTractionMarkers(null);
        setCafeMarkers(null);
        setRestaurantMarkers(null);
        setLodgingMarkers(planmap);
    }
    planmap.setBounds(planMapbounds);
}

function openPlanMap() {
    planmap = new kakao.maps.Map(planmapContainer, planmapOption);
    document.getElementById("searchPlace").value="";
    categorys[0].click();
    changeMarker("total");
}

function addPlanMarker(position, idx, title) {
    // let imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png'; // 마커 이미지 url, 스프라이트 이미지를 씁니다
    let imageSrc = '/image/category_img.png'; // 마커 이미지 url, 스프라이트 이미지를 씁니다
    let imageSize = new kakao.maps.Size(36, 37);  // 마커 이미지의 크기
    let imgOptions = {
        spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
        spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
        offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
    };
    let markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions);
    let marker = new kakao.maps.Marker({
        position: position, // 마커의 위치
        image: markerImage
    });

    marker.setMap(planmap); // 지도 위에 마커를 표출합니다
    planmarkers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

function changeBox(index){
    const selectSpan = document.querySelectorAll("#selectBtnBox span");
    const placeBox = document.getElementsByClassName("placeBox");
    const planMap = document.getElementById("planMap");
    const selectMap = document.getElementById("map");

    selectSpan[index%2].classList.add("select");
    selectSpan[(index+1)%2].classList.remove("select");
    placeBox[index%2].classList.remove("disappear");
    placeBox[(index+1)%2].classList.add("disappear");

    if(index==0){
        planMap.classList.remove("disappear");
        selectMap.classList.add("disappear");
        openPlanMap();
    }else{
        planMap.classList.add("disappear");
        selectMap.classList.remove("disappear");
        openSelectMap();
    }
}

function sizeSelect(index){
    const placeListBox = document.getElementById("placeListBox");
    const planMap = document.getElementById("planMap");
    const selectMap = document.getElementById("map");
    const map = (selectMap.classList.contains("disappear")?planMap:selectMap);
    const othermap = (selectMap.classList.contains("disappear")?selectMap:planMap);
    const btn = document.querySelector("#placeListBox span");
    const className = ["bigListBox","smallListBox"];
    const animationName = ["addSize","removeSize"];
    const btnClass = ["prev","next"];
    const mapClassName = ["smallMap","largeMap"];
    const mapAnimationName = ["removeSizeMap","addSizeMap"];

    placeListBox.classList.add(animationName[index]);
    map.classList.add(mapAnimationName[index]);
    // console.log(map);
    function finishAnimation(event){
        map.className="";
        map.classList.add(mapClassName[index]);
        othermap.className="disappear"
        othermap.classList.add(mapClassName[index]);
        placeListBox.className="";
        placeListBox.classList.add(className[index]);
        btn.className =""
        btn.classList.add("controll","btn", btnClass[index]);
        btn.setAttribute("onclick","sizeSelect("+(index%2==0?1:0)+")");
        placeListBox.removeEventListener('animationend', finishAnimation);
    }
    placeListBox.addEventListener('animationend', finishAnimation);
}


const stepbtns = document.getElementsByClassName("stepsign");
const stepBoxs = document.getElementsByClassName("stepBox");
const categoryNameList = new Map();
categoryNameList.set("음식점","식당");
categoryNameList.set("음식점_카페","카페");
categoryNameList.set("관광명소","명소");
categoryNameList.set("식당","restaurant");
categoryNameList.set("카페","cafe");
categoryNameList.set("명소","traction");
categoryNameList.set("숙소","lodging");

function showSelectBox(index) {
    if(index===3){
        planFormSubmit.classList.remove("disappear");
        nextBtn.classList.add("disappear");
    }else{
        planFormSubmit.classList.add("disappear");
        nextBtn.classList.remove("disappear");
    }
    const box = stepBoxs[index];
    if (!box.classList.contains("disappear")) {
        return;
    }
    for (let i = 0; i < stepbtns.length; i++) {
        if (index == i) {
            stepbtns[i].classList.remove("btn");
            stepBoxs[i].classList.remove("disappear");
            stepBoxs[i].classList.add("showStepBoxAnimation");
            stepbtns[i].removeAttribute("onclick");
        } else {
            stepbtns[i].classList.add("btn");
            stepBoxs[i].classList.add("disappear");
            stepBoxs[i].classList.remove("showStepBoxAnimation");
            stepbtns[i].setAttribute("onclick","showSelectBox("+i+")");
        }
    }
    if(index===2) {
        openPlanMap();
    }
}


const nowDate = new Date();
const nowYear = Number(nowDate.getFullYear());
const nowMonth = Number(nowDate.getMonth() + 1);
const nowDay = Number(nowDate.getDate());

// 달력에 오늘 날짜 지정
// view에 나타나 있는 달력 2개의 년, 월이 담긴 span태그 4개
const setDate = document.querySelectorAll(".dateBox span");
setDate[0].innerText = nowYear;
setDate[1].innerText = nowMonth;
setDate[2].innerText = nowYear + (nowMonth == 12 ? 1 : 0);
setDate[3].innerText = (nowMonth == 12 ? 1 : nowMonth + 1);

// 여행 시작날, 종료날
const startdate = document.getElementById("startdate");
const enddate = document.getElementById("enddate");
const planDays = document.getElementsByClassName("dayOfWeek");

let dayOfMonth = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
const dayOfWeek = ["일", "월", "화", "수", "목", "금", "토"];
const celendars = document.getElementsByClassName("celendar");
celendars[0].innerHTML = setCelendar(nowYear, nowMonth);
celendars[1].innerHTML = setCelendar(nowYear + Number(`${nowMonth == 12 ? 1 : 0}`), `${nowMonth == 12 ? 1 : Number(nowMonth + 1)}`);

function resetDays() {
    const days = document.getElementsByClassName("day");
    for (let day of days) {
        day.classList.remove("select");
        day.classList.remove("involve");
    }
}

function checkPlan() {
    const startdates = [Number(startdate.value.split(".")[0]), Number(startdate.value.split(".")[1]),Number(startdate.value.split(".")[2])]; // 일정시작날
    const enddates = [Number(enddate.value.split(".")[0]), Number(enddate.value.split(".")[1]),Number(enddate.value.split(".")[2])]; // 일정끝나는날

    if(startdate.value==enddate.value){
        return;
    }
    let selectCnt = document.getElementsByClassName("select").length;
    const days = document.getElementsByClassName("day");
    const celendarDate = [Number(setDate[0].innerText), Number(setDate[1].innerText)];
    // console.log(celendarDate);
    // 좌, 우 캘린더에서 좌 캘린더 년,월보다 시작날이 더 빠르면 true 예시) startDate(2024-05) < CelenderDate(2024-06) - true
    let check1 = ((celendarDate[0] - startdates[0]) * 365 + (celendarDate[1] - startdates[1]) * 30) > 0 ? true : false;
    // 좌, 우 캘린더에서 좌 캘린더 년,월보다 종료날이 더 느리면 true 예시) endDate(2024-07) > CelenderDate(2024-06) - true
    let check2 = ((celendarDate[0] - enddates[0]) * 365 + (celendarDate[1] - enddates[1]) * 30) < 0 ? true : false;

    let flag = false;
    if (check1) {
        flag = true;
    }
    if (check2) {
        selectCnt += 1;
    }

    for (let day of days) {
        if (day.classList.contains("select")) {
            selectCnt -= 1;
            flag = !flag;
        } else if (flag) {
            day.classList.add("involve");
        }
        if (selectCnt == 0) {
            break;
        }
    }
}


function addEventToDays() {
    const celendar1 = document.querySelectorAll(".celendar.one .day");
    const celendar2 = document.querySelectorAll(".celendar.two .day");

    const startdates = startdate.value.split(".");
    const enddates = enddate.value.split(".");

    for (let item of celendar1) {
        if (Number(setDate[0].innerText) == Number(startdates[0]) && Number(setDate[1].innerText) == Number(startdates[1]) && Number(item.innerText) == Number(startdates[2])) {
            item.classList.add("select");
        } else if (Number(setDate[0].innerText) == Number(enddates[0]) && Number(setDate[1].innerText) == Number(enddates[1]) && Number(item.innerText) == Number(enddates[2])) {
            item.classList.add("select");
            checkPlan();
        }
        item.addEventListener("click", function (e) {
            e.preventDefault();
            const day = e.target;
            let str = setDate[0].innerText + "." + (setDate[1].innerText.length == 1 ? "0" : "") + setDate[1].innerText + "." + (day.innerText.length == 1 ? "0" : "") + day.innerText;
            let selectStr = setDate[0].innerText + "-" + (setDate[1].innerText.length == 1 ? "0" : "") + setDate[1].innerText + "-" + (day.innerText.length == 1 ? "0" : "") + day.innerText;
            const selectDate = new Date(selectStr);
            if (startdate.value == "") {
                startdate.value = str;
                planDays[0].innerText = "(" + dayOfWeek[selectDate.getDay()] + ")";
                day.classList.add("select");
            } else if (enddate.value == "") {
                const startValue = startdate.value.split(".");
                const endValue = str.split(".");
                const value = (Number(endValue[0]) - Number(startValue[0])) * 365 + (Number(endValue[1]) - Number(startValue[1])) * 30 + (Number(endValue[2]) - Number(startValue[2]));

                if (value > 9) {
                    alert("여행 일자는 최대 10일까지 설정 가능합니다.");
                } else if (value >= 0) {
                    enddate.value = str;
                    planDays[1].innerText = "(" + dayOfWeek[selectDate.getDay()] + ")";
                    day.classList.add("select");
                    addScheduleDay();
                    checkPlan();
                    showSelectBox(2);
                } else {
                    resetDays();
                    startdate.value = str;
                    planDays[0].innerText = "(" + dayOfWeek[selectDate.getDay()] + ")";
                    day.classList.add("select");
                }
            } else {
                resetDays();
                startdate.value = str;
                planDays[0].innerText = "(" + dayOfWeek[selectDate.getDay()] + ")";
                enddate.value = "";
                planDays[1].innerText = "";
                day.classList.add("select");
            }
        })
    }

    for (let item of celendar2) {
        if (Number(setDate[2].innerText) == Number(startdates[0]) && Number(setDate[3].innerText) == Number(startdates[1]) && Number(item.innerText) == Number(startdates[2])) {
            item.classList.add("select");
            checkPlan();
        } else if (Number(setDate[2].innerText) == Number(enddates[0]) && Number(setDate[3].innerText) == Number(enddates[1]) && Number(item.innerText) == Number(enddates[2])) {
            item.classList.add("select");
            checkPlan();
        }

        item.addEventListener("click", function (e) {
            e.preventDefault();
            const day = e.target;
            let str = setDate[2].innerText + "." + (setDate[3].innerText.length == 1 ? "0" : "") + setDate[3].innerText + "." + (day.innerText.length == 1 ? "0" : "") + day.innerText;
            let selectStr = setDate[2].innerText + "-" + (setDate[3].innerText.length == 1 ? "0" : "") + setDate[3].innerText + "-" + (day.innerText.length == 1 ? "0" : "") + day.innerText;
            const selectDate = new Date(selectStr);
            if (startdate.value == "") {
                startdate.value = str;
                planDays[0].innerText = "(" + dayOfWeek[selectDate.getDay()] + ")";
                day.classList.add("select");
            } else if (enddate.value == "") {
                const startValue = startdate.value.split(".");
                const endValue = str.split(".");
                const value = (Number(endValue[0]) - Number(startValue[0])) * 365 + (Number(endValue[1]) - Number(startValue[1])) * 30 + (Number(endValue[2]) - Number(startValue[2]));

                if (value > 9) {
                    alert("여행 일자는 최대 10일까지 설정 가능합니다.");
                } else if (value >= 0) {
                    enddate.value = str;
                    planDays[1].innerText = "(" + dayOfWeek[selectDate.getDay()] + ")";
                    day.classList.add("select");
                    addScheduleDay();
                    checkPlan();
                    showSelectBox(2);
                } else {
                    resetDays();
                    startdate.value = str;
                    planDays[0].innerText = "(" + dayOfWeek[selectDate.getDay()] + ")";
                    day.classList.add("select");
                }
            } else {
                resetDays();
                startdate.value = str;
                planDays[0].innerText = "(" + dayOfWeek[selectDate.getDay()] + ")";
                enddate.value = "";
                planDays[1].innerText = "";
                day.classList.add("select");
            }

        })
    }

}

addEventToDays();

// 달력 이동 버튼 이벤트 추가 시작
const prevmonth = document.getElementById("prevmonth");
const nextmonth = document.getElementById("nextmonth");
prevmonth.addEventListener("click", function () {
    const date = document.querySelectorAll(".dateBox span");
    let year = Number(date[0].innerText) - (Number(date[1].innerText) == 1 ? 1 : 0);
    let month = (Number(date[1].innerText) == 1 ? 12 : Number(date[1].innerText) - 1);
    celendars[0].innerHTML = setCelendar(year, month);
    celendars[1].innerHTML = setCelendar(date[0].innerText, date[1].innerText);
    date[2].innerText = date[0].innerText;
    date[3].innerText = date[1].innerText;
    date[0].innerText = year;
    date[1].innerText = month;
    addEventToDays();
})
nextmonth.addEventListener("click", function () {
    const date = document.querySelectorAll(".dateBox span");
    let year = Number(date[2].innerText) + (Number(date[3].innerText) == 12 ? 1 : 0);
    let month = (Number(date[3].innerText) == 12 ? 1 : Number(date[3].innerText) + 1);
    celendars[0].innerHTML = setCelendar(date[2].innerText, date[3].innerText);
    celendars[1].innerHTML = setCelendar(year, month);
    date[0].innerText = date[2].innerText;
    date[1].innerText = date[3].innerText;
    date[2].innerText = year;
    date[3].innerText = month;
    addEventToDays();
})

// 달력 이동 버튼 이벤트 추가 끝
function setCelendar(year, month, flag) {
    const str = year + "-" + (month.length == 1 ? "0" + month : month) + "-01";// ex : '2024-05-01'
    const date = new Date(str);// str 날의 정보들 - 요일
    const daynum = date.getDay();
    let days = "<tr><th>일</th><th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th>토</th></tr>";
    days += "<tr>"
    // true이면 달력 빈 공간에도 일자 표시
    if (flag) {
        for (let i = daynum - 1; i >= 0; i--) {
            days += `<th class=${1 > 0 ? 'day' : '0'}>${dayOfMonth[(month - 1 == 0 ? 12 : day - 1)] - i}</th>`
        }
    } else { // false이면 달력 빈 공간에 일자 표시X
        for (let i = daynum - 1; i >= 0; i--) {
            days += `<th></th>`
        }
    }
    const value = (year - nowYear) * 365 + (month - nowMonth) * 30;
    for (let i = 1; i <= dayOfMonth[month] + (((year % 400 == 0 || (year % 100 != 0 && year % 4 == 0)) && month == 2) ? 1 : 0); i++) {
        if ((i + daynum) % 7 == 1) {
            if (i != 1) {
                days += "</tr><tr>";
            }
            days += `<th class='${value + (i - nowDay) >= 0 ? 'day sun' : 'past'}'>${i}</th>`;
        } else if ((i + daynum) % 7 == 0) {
            days += `<th class='${value + (i - nowDay) >= 0 ? 'day sat' : 'past'}'>${i}</th>`;
        } else {
            days += `<th class='${value + (i - nowDay) >= 0 ? 'day' : 'past'}'>${i}</th>`;
        }
    }
    days += "</tr>";
    return days;
}

function textOverFlow() {
    const placenames = document.getElementsByClassName("placename");
    const placeaddrs = document.getElementsByClassName("placeaddr");

    for (let i = 0; i < placenames.length; i++) {
        const placename = placenames[i];
        const placeaddr = placeaddrs[i];

        if (placename.innerText.length > 11) {
            placename.innerText = placename.innerText.substring(0, 10) + "...";
        }
        if (placeaddr.innerText.length > 15) {
            placeaddr.innerText = placeaddr.innerText.substring(0, 13) + "...";
        }
    }
}

// 장소 추가. 삭제 이벤트 구현
let selectList = [];
const selectListBox = document.getElementById("selectList");
const placeCnt = document.getElementById("placeCnt");

function addPlanList(index) {
    const placeList = document.querySelectorAll("#placeList .place");
    const place = placeList[index];
    const placeid = place.children[0].value;

    // placeid 값을 planList 에 저장
    if (selectList.includes(placeid)) {
        return;
    }
    selectList.push(placeid);

    const btn = place.children[3];
    btn.classList.remove("add");
    btn.classList.add("checked");
    btn.setAttribute("onclick", "removePlanList(" + index + ")");
    btn.innerText = "✔";

    const liTag = document.createElement("li");
    liTag.classList.add("row");
    liTag.setAttribute("id", "placeid_" + placeid);

    const spanTag = document.createElement("span");
    spanTag.classList.add("selectnum");
    spanTag.innerText = selectList.indexOf(placeid) + 1;
    liTag.appendChild(spanTag);

    liTag.appendChild(place.children[1].cloneNode(true));
    liTag.appendChild(place.children[2].cloneNode(true));
    const placeidBox = place.children[0].cloneNode(true);
    placeidBox.removeAttribute("class");
    placeidBox.setAttribute("name","placeidList");
    liTag.appendChild(placeidBox);

    selectListBox.appendChild(liTag);
    placeCnt.innerText = selectList.length;
    refreshPlan();
}

function removePlanList(index) {
    const placeList = document.querySelectorAll("#placeList .place");
    const place = placeList[index];
    const placeid = place.children[0].value;

    // placeid 값을 planList 에 삭제
    if (!selectList.includes(placeid)) {
        return;
    }
    selectList.splice(selectList.indexOf(placeid), 1);

    const btn = place.children[3];
    btn.classList.remove("checked");
    btn.classList.add("add");
    btn.setAttribute("onclick", "addPlanList(" + index + ")");
    btn.innerText = "+";

    const removePlace = document.getElementById("placeid_" + placeid);
    removePlace.remove();

    placeCnt.innerText = selectList.length;
    refreshPlan();
}


// 장소 추가/삭제 할때마다 좌표 새로고침 하는 함수
async function refreshPlan() {
    totalPositions = [];
    cafePositions = [];
    restaurantPositions = [];
    lodgingPositions = [];
    tractionPositions = [];
    totalMarkers = [];
    tractionMarkers = [];
    cafeMarkers = [];
    restaurantMarkers = [];
    lodgingMarkers = [];
    const list = selectListBox.children;
    const promises = []; // 비동기 작업을 저장할 배열

    for (let i = 0; i < list.length; i++) {
        const place = list[i];
        place.children[0].innerText = i + 1;
        const category = place.children[2].children[2].innerText;
        const placeaddr = place.children[2].children[3].innerText;
        const promise = getCoordinate(placeaddr).then(coordinate => {
            //console.log(new kakao.maps.LatLng(coordinate[0], coordinate[1]));
            const latlng = new kakao.maps.LatLng(coordinate[0], coordinate[1]);
            totalPositions.push(latlng);
            if(category=="카페"){
                cafePositions.push(latlng);
            }else if(category=="식당"){
                restaurantPositions.push(latlng);
            }else if(category=="명소"){
                tractionPositions.push(latlng);
            }else if(category=="숙소"){
                lodgingPositions.push(latlng);
            }
        }).catch(error => {
            console.error(error);
        });
        promises.push(promise); // 배열에 추가된 비동기 작업을 추가
    }
    // 모든 비동기 작업이 완료될 때까지 기다림
    await Promise.all(promises);

    createTotalMarkers();
    createCafeMarkers(); // 카페 마커를 생성하고 식당 마커 배열에 추가합니다
    createRestaurantMarkers(); // 식당 마커를 생성하고 식당 마커 배열에 추가합니다
    createTractionMarkers();
    createLodgingMarkers(); // 숙소 마커를 생성하고 숙소 마커 배열에 추가합니다

    // 모든 비동기 작업이 완료된 후에 실행
    changeMarker("total");
}

// 주소로 좌표 리턴
// function getCoordinate(address){
//     console.log(address);
//     var geocoder = new kakao.maps.services.Geocoder();
//
//     geocoder.addressSearch(address, function(result, status) {
//         // 정상적으로 검색이 완료됐으면
//         if (status === kakao.maps.services.Status.OK) {
//             return new kakao.maps.LatLng(result[0].y, result[0].x);
//         }
//     });
// }

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

// 카카오 장소 검색 API
// 마커를 담을 배열입니다
let markers = [];
let latitude;// 위도
let longitude;// 경도
let mapContainer = document.getElementById('map'); // 지도를 표시할 div
let mapOption= {//지도 옵션
    center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
    level: 3 // 지도의 확대 레벨
};

// 현재위치 위도 경도 받아오기
// HTML5의 geolocation으로 사용할 수 있는지 확인합니다
if (navigator.geolocation) {
    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
    navigator.geolocation.getCurrentPosition(function (position) {
        latitude = position.coords.latitude; // 위도
        longitude = position.coords.longitude; // 경도
        mapOption = {
            center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };
        planmapOption = {
            center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
            level: 5 // 지도의 확대 레벨
        };
    });
}else{
    mapOption = {
        center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
        level: 3 // 지도의 확대 레벨
    };
}



// 지도를 생성합니다
let map = new kakao.maps.Map(mapContainer, mapOption);

// 장소 검색 객체를 생성합니다
let ps = new kakao.maps.services.Places();

// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
let infowindow = new kakao.maps.InfoWindow({zIndex: 1});

// 키워드 검색에 넎을 옵션
let kwdOption = {
    size: 10
};

// 키워드 검색을 요청하는 함수입니다
function searchPlaces() {
    let keyword = document.getElementById('seach').value;
    // const region = document.querySelector("#regionCatgory .region .categoryText");
    // if(region != undefined){
    //     keyword = region.innerText+" "+keyword;
    // }
    if (!keyword.replace(/^\s+|\s+$/g, '')) {
        alert('키워드를 입력해주세요!');
        return false;
    }

    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
    ps.keywordSearch(keyword, placesSearchCB, kwdOption);
}

// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
    if (status === kakao.maps.services.Status.OK) {
        // 정상적으로 검색이 완료됐으면
        // 검색 목록과 마커를 표출합니다
        displayPlaces(data);
        // 페이지 번호를 표출합니다
        displayPagination(pagination);
        // textOverFlow();
    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
        alert('검색 결과가 존재하지 않습니다.');


    } else if (status === kakao.maps.services.Status.ERROR) {
        alert('검색 결과 중 오류가 발생했습니다.');


    }
}

// 검색 결과 목록과 마커를 표출하는 함수입니다
function displayPlaces(places) {

    var listEl = document.getElementById('placesList'),
        menuEl = document.getElementById('menu_wrap'),
        fragment = document.createDocumentFragment(),
        bounds = new kakao.maps.LatLngBounds(),
        listStr = '';

    // 검색 결과 목록에 추가된 항목들을 제거합니다
    removeAllChildNods(listEl);

    // 지도에 표시되고 있는 마커를 제거합니다
    removeMarker();
    if(places.length !=0){
        for (let i = 0; i < places.length; i++) {
            // 마커를 생성하고 지도에 표시합니다
            var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
                marker = addMarker(placePosition, i),
                itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다
            console.log(places[i].category_name);
            // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
            // LatLngBounds 객체에 좌표를 추가합니다
            bounds.extend(placePosition);

            // 마커와 검색결과 항목에 mouseover 했을때
            // 해당 장소에 인포윈도우에 장소명을 표시합니다
            // mouseout 했을 때는 인포윈도우를 닫습니다
            (function (marker, place) {
                // mouseover
                kakao.maps.event.addListener(marker, 'click', function () {
                    displayInfowindow(marker, place);
                });

                // kakao.maps.event.addListener(marker, 'mouseout', function () {
                //     infowindow.close();
                // });

                itemEl.onmouseover = function () {
                    displayInfowindow(marker, place);
                };

                itemEl.onmouseout = function () {
                    infowindow.close();
                };
            })(marker, places[i]);
            // console.log(places[i]);

            fragment.appendChild(itemEl);
        }
        // 검색결과 항목들을 검색결과 목록 Element에 추가합니다
        listEl.appendChild(fragment);
        menuEl.scrollTop = 0;
    }else{
        const noticeLi = `<li class="noplace">
                               <span>검색 결과가 없습니다</span>
                            </li>`;
        listEl.innerHTML = noticeLi;
    }
    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
    map.setBounds(bounds);
}

// 검색결과 항목을 Element로 반환하는 함수입니다
function getListItem(index, place) {
    // console.log(place);
    let el = document.createElement('li');
    let itemStr = '<span class="markerbg marker_' + (index + 1) + '"></span>' +
            '<div class="placeInfo">' +
            '   <p class="placename">' + place.place_name + '</p>';

    if (place.road_address_name) {
        itemStr += '    <span class="placeaddr">' + place.road_address_name + '</span>' +
            '   <span class="jibun gray" hidden="">' + place.address_name + '</span>';
    } else {
        itemStr += '    <span class="placeaddr">' + place.address_name + '</span>';
    }

    itemStr += '  <span class="tel">' + place.phone + '</span>' +
        '</div>';

    el.innerHTML = itemStr;
    el.className = 'place btn';
    el.addEventListener("click",function(e) {
        e.preventDefault();
        const addPlaceForm = document.getElementById("addPlaceForm");
        let infoStr = `<input type="hidden" id="addPlaceid" value="` + place.id + `">`;
        infoStr += `<input type="text" id="addPlacename" value="` + place.place_name + `" readonly>`;
        infoStr += `<span id="favoriteBtn" class="btn" onclick="favoriteCheck(false)"></span>`;
        const categoryList = place.category_name.split(">");
        let category = categoryList[0].trim();
        // let key = categoryList[0].trim()+(categoryList[1].trim()=="카페"?"_카폐":"");
        let key = categoryList[0].trim().split(",");
        for (let i = 1; i < categoryList.length; i++) {
            category += "/&&/" + categoryList[i].trim();
        }
        infoStr += `<input type="hidden" id="addCategory" value="` + category + `">`;

        infoStr += `<input type="text" id="showCategoryArea" value="` + key[0] + `#` + categoryList[categoryList.length
        - 1].trim() + `" readonly>`;
        let address = (place.road_address_name == "" || place.road_address_name == undefined) ? place.address_name : place.road_address_name;
        infoStr += `<input type="text" id="addPhone" value="` + place.phone + `" readonly>`;
        infoStr += `<input type="text" id="addAddr" value="` + address + `" readonly>`;
        infoStr += `<input type="hidden" id="order" value="false">`;
        infoStr += `<select id="categorySelectBox">
            <option value="">카테고리를 선택해주세요</option>
            <option value="명소">명소</option>
            <option value="식당">식당</option>
            <option value="카페">카페</option>
            <option value="숙소">숙소</option>
        </select>`;
        infoStr += `<input type="button" id="cencelBtn" class="btn" value="취소" onclick="cancelAdd()">`;
        infoStr += `<input type="submit" id="submitBtn" class="btn" value="확인" onclick="addPlace()">`;
        addPlaceForm.innerHTML = infoStr;

        const opacityBox = document.getElementById("opacityBox");
        opacityBox.classList.remove("disappear");
        addPlaceForm.classList.remove("disappear");
    })

    return el;
}

// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
function addMarker(position, idx, title) {
    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
        imgOptions = {
            spriteSize: new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
            spriteOrigin: new kakao.maps.Point(0, (idx * 46) + 10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
        },
        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
        marker = new kakao.maps.Marker({
            position: position, // 마커의 위치'/image/category_img.png'
            image: markerImage
        });

    marker.setMap(map); // 지도 위에 마커를 표출합니다
    markers.push(marker);  // 배열에 생성된 마커를 추가합니다

    return marker;
}

// 지도 위에 표시되고 있는 마커를 모두 제거합니다
function removeMarker() {
    for (var i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
    markers = [];
}

// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
function displayPagination(pagination) {
    var paginationEl = document.getElementById('pagination'),
        fragment = document.createDocumentFragment(),
        i;

    // 기존에 추가된 페이지번호를 삭제합니다
    while (paginationEl.hasChildNodes()) {
        paginationEl.removeChild(paginationEl.lastChild);
    }
    // console.log(pagination);
    for (i = 1; i <= pagination.last; i++) {
        var el = document.createElement('a');
        el.href = "#";
        el.innerHTML = i;

        if (i === pagination.current) {
            el.className = 'on';
        } else {
            el.onclick = (function (i) {
                return function () {
                    pagination.gotoPage(i);
                }
            })(i);
        }

        fragment.appendChild(el);
    }
    paginationEl.appendChild(fragment);
}

// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
// 인포윈도우에 장소명을 표시합니다
function displayInfowindow(marker, place) {
    var content = '<div class="contentInfoView">';
    content += `<p class="title">`+place.place_name+`</p>`;
    content += `<p class="addr">`+place.address_name+`</p>`;
    content += `<p class="phone">`+place.phone+`</p>`;
    content += `<input type="button" class="aTag" value="장소 자세히보기" onclick="showIframe('`+place.place_url+`');">`;
    content += '</div>';

    infowindow.setContent(content);
    infowindow.open(map, marker);
}

// 검색결과 목록의 자식 Element를 제거하는 함수입니다
function removeAllChildNods(el) {
    while (el.hasChildNodes()) {
        el.removeChild(el.lastChild);
    }
}

function openSelectMap() {
    map = new kakao.maps.Map(mapContainer, mapOption);
    document.getElementById("seach").value = "";
    document.getElementById("placesList").innerHTML = `<li class="noplace">
                               <span>장소를 검색해주세요</span>
                            </li>`;
    document.getElementById("pagination").innerHTML = "";
}

function showIframe(url){
    const box =document.getElementById("opacityBox");
    const iframe = document.getElementById("iframeBox");
    const offBtn = document.getElementById("offIframeBtn");
    iframe.setAttribute("src",url);
    offBtn.classList.remove("disappear");
    iframe.classList.remove("disappear");
    box.classList.remove("disappear");
}

function offIframe(){
    const box =document.getElementById("opacityBox");
    const iframe = document.getElementById("iframeBox");
    const offBtn = document.getElementById("offIframeBtn");
    box.classList.add("disappear");
    iframe.removeAttribute("src");
    iframe.classList.add("disappear");
    offBtn.classList.add("disappear");
}

function favoriteCheck(flag){
    const favoriteBtn = document.getElementById("favoriteBtn");
    const order = document.getElementById("order");

    if(flag){
        favoriteBtn.classList.remove("checked");
        favoriteBtn.setAttribute("onclick","favoriteCheck(false)");
        order.value = "false";
    }else{
        favoriteBtn.classList.add("checked");
        favoriteBtn.setAttribute("onclick","favoriteCheck(true)");
        order.value = "true";
    }

    const test = document.getElementById("categorySelectBox");
    // console.log(test.value);
}

function cancelAdd(){
    const opacityBox = document.getElementById("opacityBox");
    const addPlaceForm = document.getElementById("addPlaceForm");
    opacityBox.classList.add("disappear");
    addPlaceForm.innerHTML = "";
    addPlaceForm.classList.add("disappear");
}

function showNotice(order){
    let noticetext;
    if(order == "O"){
        noticetext = "장소가 추가되었습니다";
    }else if(order = "AF"){
        noticetext = "즐겨찾기에 추가되었습니다";
    }else if(order = "EAF"){
        noticetext = "이미 즐겨찾기에 추가되어있습니다";
    }else if(order = "E"){
        noticetext = "이미 등록된 자송입니다";
    }

    const noticeBox = document.getElementById("addPlaceForm");
    const placename = document.getElementById("addPlacename").value;
    const addr = document.getElementById("addAddr").value;
    const phone = document.getElementById("addPhone").value;
    const category = document.getElementById("showCategoryArea").value;
    let innerStr = `<input type="text" id="addPlacename" value="`+placename+`" readonly="">`;
    innerStr += `<input type="text" id="showCategoryArea" value="`+category+`" readonly="">`;
    innerStr += `<input type="text" id="addPhone" value="`+phone+`" readonly="">`;
    innerStr += `<input type="text" id="addAddr" value="`+addr+`" readonly="">`;
    innerStr += `<span id="noticeText">`+noticetext+`</span>`;
    innerStr += `<input type="button" id="offBoxBtn" class="btn" value="확인" onclick="cancelAdd()">`;
    noticeBox.innerHTML = innerStr;
}

function addPlace(){
    const placeid = document.getElementById("addPlaceid").value;
    const placename = document.getElementById("addPlacename").value;
    const addr = document.getElementById("addAddr").value;
    const phone = document.getElementById("addPhone").value;
    const category = document.getElementById("addCategory").value;
    const order = document.getElementById("order").value;
    const categoryTag = document.getElementById("categorySelectBox").value;

    if(categoryTag == "") {
        alert("카테고리를 선택해주세요");
        return;
    }
    console.log(1);
    showLoding();
    console.log(2);
    $.ajax({
        url: "/place/add",
        data: {
            placeid: placeid,
            placename: placename,
            addr: addr,
            phone: phone,
            category: category,
            order: order,
            categoryTag : categoryTag
        },
        method: "POST",
        dataType: "text",
        success: function (data) {
            console.log(3);
            if(data == "X"){
                alert("문제가 발생되었습니다 다시 시도해 주세요");
            }else{
                showNotice(data);
            }
        },
        error: function (data) {
            console.log("/place/add 에러 발생");
            cancelAdd();
        }
    })
}

function getPlaceList(pagenum){
    const placeList = document.getElementById("placeList");
    const pageBtnBox = document.getElementById("cri");
    const keyword = document.getElementById("searchPlace").value;
    const type = document.querySelector("#categorySelect .select").innerText;
    // console.log(keyword);
    // console.log(type);
    $.ajax({
        url: "/place/getList",
        data: {
            type: type,
            keyword: keyword,
            pagenum: pagenum,
            amount: 10,
        },
        method: "POST",
        dataType: "json",
        success: function (data) {
            console.log(data);
            if(data == "notLogin"){
                alert("문제가 발생되었습니다 다시 시도해 주세요");
            }else{
                const places = data.placeList;
                const imgs = data.imgList;
                const likeCnt = data.likeCntList;
                const pageMaker = data.pageMaker;
                if(places.length!=0){
                    let listStr = "";
                    for(let i = 0;i<places.length;i++){
                        const place = places[i];
                        // console.log(place);
                        listStr += `<li class="place"><input type="hidden" class="placeid" value="${place.placeid}">`;
                        listStr += `<img src="`+imgs[i]+`">`;
                        listStr += `<div class="placeInfo">`;
                        listStr += `<p class="placename">${place.placename}</p>`;
                        listStr += `<input type="hidden" class="category" value="${place.category}">`;
                        listStr += `<p class="category">${place.category.split("/&&/")[0]}</p>`;
                        listStr += `<p class="placeaddr">${place.addr}</p>`;
                        listStr += `<p class="placepoint">`;
                        listStr += `<img src="/image/heart_img.png"><span>${likeCnt[i]}</span>`;
                        listStr += `</p></div>`;
                        if (selectList.includes(place.placeid+"")) {
                            listStr += `<span class="btn checked" onclick="removePlanList(${i})">✔</span></li>`;
                        }else{
                            listStr += `<span class="btn add" onclick="addPlanList(${i})">+</span></li>`;
                        }
                    }
                    placeList.innerHTML = listStr;

                    let pageStr = "";
                    if(pageMaker.prev){
                        pageStr += `<li class="btn prev" onclick="getPlaceList(${pageMaker.startPage-1})"></li>`;
                    }else{
                        pageStr += `<li class="noBtn"></li>`;
                    }
                    for(let i = pageMaker.startPage ;i<=pageMaker.endPage;i++){
                        pageStr += `<li `;
                        if(pageMaker.cri.pagenum == i){
                            pageStr += `class="btn select">`;
                        }else{
                            pageStr += `class="btn" onclick="getPlaceList(${i})">`;
                        }
                        pageStr += i;
                        pageStr += `</li>`;
                    }
                    if(pageMaker.next){
                        pageStr += `<li class="btn next" onclick="getPlaceList(${pageMaker.endPage+1})"></li>`;
                    }else{
                        pageStr += `<li class="noBtn"></li>`;
                    }
                    pageBtnBox.innerHTML = pageStr;
                }else{
                    placeList.innerHTML = `<li class="noplace">
                                <span>검색 결과가 없습니다</span>
                            </li>`;
                    pageBtnBox.innerHTML == 0;
                }
            }
        },
        error: function (data) {
            console.log("/place/getList 에러 발생");
            cancelAdd();
        }
    })
}

function showLoding(){
    const lodingWindow = document.getElementById("lodingWindow");
    lodingWindow.classList.remove("disappear");
    const lodingBar = document.getElementById("lodinggauge");
    lodingBar.classList.add("animation");
    lodingBar.addEventListener("animationend",()=>{
        lodingBar.classList.remove("animation");
        lodingWindow.classList.add("disappear");
    })
}

// 0529
const scheduletitleText  = document.getElementById("scheduletitleText");
const schedulememoText  = document.getElementById("schedulememoText");
const startHour = document.getElementById("startHour");
const startMin = document.getElementById("startMin");
const endHour = document.getElementById("endHour");
const endMin = document.getElementById("endMin");
const starttimeText =document.getElementById("starttimeText");
const endtimeText =document.getElementById("endtimeText");
const selectColorBox = document.getElementById("colors");

selectColorBox.addEventListener("change",function(e){
    e.preventDefault();
    const colorPicker = document.getElementById("colorPicker");
    colorPicker.style.backgroundColor = selectColorBox.value;
})

startHour.addEventListener("change",function(e){
    e.preventDefault();
    starttimeText.value = startHour.value+":"+starttimeText.value.split(":")[1];
})
startMin.addEventListener("change",function(e){
    e.preventDefault();
    starttimeText.value = starttimeText.value.split(":")[0]+":"+startMin.value;
})
endHour.addEventListener("change",function(e){
    e.preventDefault();
    endtimeText.value = endHour.value+":"+endtimeText.value.split(":")[1];
    let satrtValue = Number(starttimeText.value.split(":")[0]*60) + Number(starttimeText.value.split(":")[1]);
    let endValue = Number(endtimeText.value.split(":")[0]*60) + Number(endtimeText.value.split(":")[1]);
    if(endValue-satrtValue<0){
        alert("시간을 다시 입력해주세요")
    }
})
endMin.addEventListener("change",function(e){
    e.preventDefault();
    endtimeText.value = endtimeText.value.split(":")[0]+":"+endMin.value;
    let satrtValue = Number(starttimeText.value.split(":")[0]*60) + Number(starttimeText.value.split(":")[1]);
    let endValue = Number(endtimeText.value.split(":")[0]*60) + Number(endtimeText.value.split(":")[1]);
    if(endValue-satrtValue<0){
        alert("시간을 다시 입력해주세요")
    }else if(endValue-satrtValue<10){
        alert("10분 이상 차이가 나야 합니다")
    }
})

function addSchedule(day){
    const writeScheduleBox = document.getElementById("writeScheduleBox");
    const opacityBox = document.getElementById("opacityBox");

    const months = document.querySelectorAll("#datebunchBox .bunch");
    const days = document.querySelectorAll("#scheduleDayList .planDay");
    let date;
    if(months.length == 1){
        date = months[0].id +"_"+day;
    }else{
        const firttday = Number(days[0].innerText);
        if(firttday<=Number(day)){
            date = months[0].id +"_"+(Number(day)<10?"0"+Number(day):day);
        }else{
            date = months[1].id +"_"+(Number(day)<10?"0"+Number(day):day);
        }
    }

    // const scheduleBox = document.getElementById("planDay"+day);
    const writeOkBtn = document.getElementById("writeOkBtn");
    writeOkBtn.setAttribute("onclick","okActionEvent('"+date+"')");
    writeScheduleBox.classList.remove("disappear");
    opacityBox.classList.remove("disappear");
}

function okActionEvent(date){
    const scheduleBox = document.getElementById("planDay"+date.split("_")[2]);
    let satrtValue = Number(starttimeText.value.split(":")[0]*60) + Number(starttimeText.value.split(":")[1]);
    let endValue = Number(endtimeText.value.split(":")[0]*60) + Number(endtimeText.value.split(":")[1]);

    if(scheduletitleText.value == ""){
        alert("주제를 작성해주세요");
    }else if(schedulememoText.value == "") {
        alert("메모를 작성해주세요");
    }else if(starttimeText.value == endtimeText.value || endtimeText.value == "00:00"){
        alert("시간을 설정해주세요")
    }else if(endValue-satrtValue<0){
        alert("시간을 다시 입력해주세요")
        endtimeText.value = "00:00";
    }else if(endValue-satrtValue<10){
        alert("10분 이상 차이가 나야 합니다")
        endtimeText.value = "00:00";
    }else {
        const scheduleList = scheduleBox.children[2];
        const copy = document.getElementById("sampleDayScheduleBox").children[2];
        const scheduleLi = copy.children[0].cloneNode(true);
        const placid = document.getElementById("schedulePlaceid");
        // console.log(placid);

        const scheduleInfo = scheduleLi.children;
        scheduleInfo[0].value = scheduletitleText.value;
        // 디테일 박스 오픈 이벤트
        scheduleInfo[0].setAttribute("onclick","showSchedule('"+date+"',"+scheduleList.children.length+")");
        scheduleInfo[1].value = schedulememoText.value+(placid !=null?"/&&/"+placid.value:"");
        scheduleInfo[2].value = date+"_"+starttimeText.value;
        scheduleInfo[3].value = date+"_"+endtimeText.value;

        const startTime = starttimeText.value.split(":");
        const endTime = endtimeText.value.split(":");
        const startPixel = startTime[0]*34.56 + Number(startTime[1])*0.576;
        const endPixel = endTime[0]*34.56 + Number(endTime[1])*0.576;
        // 시간바 길이, 위치 지정
        scheduleInfo[4].style.width = (endPixel-startPixel)+"px";
        scheduleInfo[4].style.left = 151+startPixel+"px";
        scheduleInfo[4].style.backgroundColor = selectColorBox.value;
        scheduleList.appendChild(scheduleLi);
        scheduleBox.children[0].value = scheduleList.children.length;


        closeWriteScheduleBox();
    }
}

// 스케줄 작성 창 닫기
function closeWriteScheduleBox(){
    const writeScheduleBox = document.getElementById("writeScheduleBox");
    writeScheduleBox.classList.add("disappear");
    const opacityBox = document.getElementById("opacityBox");
    opacityBox.classList.add("disappear");
    const schedulePlace = document.getElementById("schedulePlace");
    schedulePlace.innerHTML = "";
    // 내부 값 초기화
    scheduletitleText.value = "";
    schedulememoText.value = "";
    startHour.children[0].setAttribute("selected",true);
    startHour.children[0].removeAttribute("selected");
    startMin.children[0].setAttribute("selected",true);
    startMin.children[0].removeAttribute("selected");
    endHour.children[0].setAttribute("selected",true);
    endHour.children[0].removeAttribute("selected");
    endMin.children[0].setAttribute("selected",true);
    endMin.children[0].removeAttribute("selected");
    starttimeText.value = "00:00";
    endtimeText.value = "00:00";
    selectColorBox.children[0].setAttribute("selected",true);
    selectColorBox.children[0].removeAttribute("selected");
    const colorPicker = document.getElementById("colorPicker");
    colorPicker.style.backgroundColor = "#00C88C";
}

// detailBox js
function showSchedule(date,index){
    const detailScheduleBox = document.getElementById("detailScheduleBox");
    const detailTitle = document.getElementById("detailTitle");
    const detailDate = document.getElementById("detailDate");
    const detailTime = document.getElementById("detailTime");
    const detailMemo = document.getElementById("detailMemo");
    const deleteScheduleBtn = document.getElementById("deleteScheduleBtn");
    const dateArray = date.split("_");

    const scheduleBox = document.getElementById("planDay"+dateArray[2]);
    const schedule = scheduleBox.children[2].children[index];
    const info = schedule.children;

    detailTitle.innerText = info[0].value;
    detailDate.innerText = dateArray[0]+"년 "+Number(dateArray[1])+"월 "+Number(dateArray[2])+"일";
    detailTime.innerText = info[2].value.split("_")[3] + " ~ "+info[3].value.split("_")[3];
    const memo = info[1].value.split("/&&/");
    const detailPlace = document.getElementById("detailPlace");
    if(memo.length==2){
        detailPlace.classList.remove("disappear");
        detailScheduleBox.classList.add("includePlace");
        const place = document.getElementById("placeid_"+memo[1]).cloneNode(true);
        place.children[0].remove();
        detailPlace.innerHTML = "";
        detailPlace.appendChild(place);
    }else{
        detailPlace.classList.add("disappear");
        detailScheduleBox.classList.remove("includePlace");
    }
    detailMemo.innerText = memo[0];
    deleteScheduleBtn.setAttribute("onclick","deleteSchedule("+dateArray[2]+","+index+")");

    const opacityBox = document.getElementById("opacityBox");
    detailScheduleBox.classList.remove("disappear");
    opacityBox.classList.remove("disappear");
}

function closeDetailBox(){
    const detailTitle = document.getElementById("detailTitle");
    const detailDate = document.getElementById("detailDate");
    const detailTime = document.getElementById("detailTime");
    const detailMemo = document.getElementById("detailMemo");
    detailTitle.innerText = "";
    detailDate.innerText = "";
    detailTime.innerText = "";
    detailMemo.innerText = "";

    const opacityBox = document.getElementById("opacityBox");
    const detailScheduleBox = document.getElementById("detailScheduleBox");
    opacityBox.classList.add("disappear");
    detailScheduleBox.classList.add("disappear");
}

// 스케줄 일정 박스 생성 함수
function addScheduleDay(){
    if(startdate.value == ""||enddate.value == ""){
        return;
    }
    const startYMD = startdate.value.split(".").map(Number);
    const endYMD = enddate.value.split(".").map(Number);

    const datebunchBox = document.getElementById("datebunchBox");
    // 스케줄 일정 박스 생성 최소1일 ~ 최대 10일
    const scheduleDayList = document.getElementById("scheduleDayList");
    const planDateText = document.getElementById("planDateText");
    const scheduleBoxList = document.getElementById("scheduleBoxList");
    scheduleBoxList.innerHTML = "";

    let str = "";
    let planDayStr = "";
    if(startYMD[1]==endYMD[1]){
        const date = startYMD[0]+"_"+(startYMD[1]<10?"0":"")+startYMD[1];
        const month = startYMD[1]+"월";
        const width = ((endYMD[2]-startYMD[2]+1)*2 - 1)*50;
        str += `<li class="bunch" id="${date}" style="width: ${width}px;">
                            <span></span>
                            <span class="yearMonth">${month}</span>
                            <span></span>
                        </li>`;
        planDayStr += `<li class="planDay select">${startYMD[2]}</li>`;
        const box = copyDayScheduleBox(startYMD[2]);
        box.classList.remove("disappear");
        scheduleBoxList.appendChild(box);
        planDateText.innerText = startYMD[0]+"년 "+startYMD[1]+"월 "+startYMD[2]+"일";
        for(let day = startYMD[2]+1;day<=endYMD[2];day++){
            planDayStr += `<li class="planDay" onclick="changeScheduleBox('${date+"_"+day}')">${day}</li>`;
            scheduleBoxList.appendChild(copyDayScheduleBox(day));
        }
    }else{
        const date1 = startYMD[0]+"_"+(startYMD[1]<10?"0":"")+startYMD[1];
        const date2 = endYMD[0]+"_"+(endYMD[1]<10?"0":"")+endYMD[1];
        const month1 = startYMD[1]+"월";
        const month2 = endYMD[1]+"월";
        let lastDay = dayOfMonth[startYMD[1]];
        if(startYMD[1]==2){
            if(startYMD[0]%4==0&&startYMD[0]%100!=0){
                lastDay++;
            }
        }
        const width1 = ((lastDay-startYMD[2]+1)*2 - 1)*50;
        const width2 = ((endYMD[2])*2 - 1)*50;
        str += `<li class="bunch" id="${date1}" style="width: ${width1}px;">
                            <span></span>
                            <span class="yearMonth">${month1}</span>
                            <span></span>
                        </li>`;
        str += `<li class="bunch" id="${date2}" style="width: ${width2}px;">
                            <span></span>
                            <span class="yearMonth">${month2}</span>
                            <span></span>
                        </li>`;
        planDayStr += `<li class="planDay select">${startYMD[2]}</li>`;
        const box = copyDayScheduleBox(startYMD[2]);
        box.classList.remove("disappear");
        scheduleBoxList.appendChild(box);
        planDateText.innerText = startYMD[0]+"년 "+startYMD[1]+"월 "+startYMD[2]+"일";
        for(let day = startYMD[2]+1;day<=lastDay;day++){
            planDayStr += `<li class="planDay" onclick="changeScheduleBox('${date1+"_"+day}')">${day}</li>`;
            scheduleBoxList.appendChild(copyDayScheduleBox(day));
        }
        for(let day = 1;day<=endYMD[2];day++){
            planDayStr += `<li class="planDay" onclick="changeScheduleBox('${date2+"_"+day}')">${day}</li>`;
            scheduleBoxList.appendChild(copyDayScheduleBox(day));
        }
    }
    datebunchBox.innerHTML = str;
    scheduleDayList.innerHTML = planDayStr;
    
//     캘린더 수정 못하게 막기
    const startdateText = document.getElementById("startdateText");
    const enddateText = document.getElementById("enddateText");
    startdateText.innerText = startdate.value;
    enddateText.innerText = enddate.value;
    document.getElementById("celendarOpacityBox").classList.remove("disappear");
    document.getElementById("scheduleOpacityBox").classList.add("disappear");
}

function copyDayScheduleBox(day){
    const box = document.getElementById("sampleDayScheduleBox").cloneNode(true);
    box.setAttribute("id","planDay"+day);
    box.children[2].innerHTML="";
    box.children[3].children[0].setAttribute("onclick","addSchedule("+day+")")
    return box;
}

function changeScheduleBox(date){
    const dateArr = date.split("_").map(Number);
    const selectDay = dateArr[2];
    const planDayList = document.getElementsByClassName("planDay");
    const planDateText = document.getElementById("planDateText");
    planDateText.innerText = dateArr[0]+"년 "+dateArr[1]+"월 "+dateArr[2]+"일";
    console.log(dateArr[0]+"년 "+dateArr[1]+"월 "+dateArr[2]+"일")
    console.log(planDateText.innerText);

    for(let planDay of planDayList){
        const day = planDay.innerText;
        const scheduleBox = document.getElementById("planDay"+day);
        if(day == selectDay){
            planDay.classList.add("select");
            planDay.removeAttribute("onclick");
            scheduleBox.classList.remove("disappear");
        }else{
            planDay.classList.remove("select");
            planDay.setAttribute("onclick","changeScheduleBox('"+dateArr[0]+"_"+dateArr[1]+"_"+day+"')");
            scheduleBox.classList.add("disappear");
        }
    }
}

function deleteSchedule(day,index){
    const dayScheduleBox = document.getElementById("planDay"+day);
    const scheduleList = dayScheduleBox.children[2].children;
    
    // 스케줄 삭제
    scheduleList[index].remove();

    for(let i=0;i<scheduleList.length;i++){
        const li = scheduleList[i];
        let event = li.children[0].getAttribute("onclick").substring(0,25)+i+")";
        li.children[0].setAttribute("onclick",event);
    }
    closeDetailBox();
}

function modifyDate(){
    startdate.value = "";
    planDays[0].innerText = "";
    enddate.value = "";
    planDays[1].innerText = "";
    resetDays();

    const startdateText = document.getElementById("startdateText");
    const enddateText = document.getElementById("enddateText");
    startdateText.innerText = "";
    enddateText.innerText = "";
    document.getElementById("celendarOpacityBox").classList.add("disappear");
    document.getElementById("scheduleOpacityBox").classList.remove("disappear");
    document.getElementById("datebunchBox").innerHTML = "";
    document.getElementById("scheduleDayList").innerHTML = "";
    document.getElementById("planDateText").innerHTML = "";
    document.getElementById("scheduleBoxList").innerHTML = "";
}

function addschedulePlace(){
    const box = document.getElementById("addSchedulePlaceBox");
    box.classList.remove("disappear");
    const schedulePlace = document.getElementById("schedulePlace");
    const list = document.getElementById("addSchedulePlaceList");
    list.innerHTML = "";
    const selectPlaceList = document.getElementById("selectList").children;
    for(let row of selectPlaceList){
        const copyRow = row.cloneNode(true);
        copyRow.children[0].remove();
        const idBox = document.createElement("input");
        idBox.setAttribute("type","hidden");
        idBox.setAttribute("id","schedulePlaceid");
        idBox.setAttribute("value",copyRow.id.split("_")[1]);
        copyRow.removeAttribute("id");
        copyRow.appendChild(idBox);
        copyRow.addEventListener("dblclick",function(){
            schedulePlace.innerHTML="";
            schedulePlace.appendChild(copyRow);
            list.innerHTML = "";
            closeAddPlaceBox();
        })
        list.appendChild(copyRow);
    }
}
function closeAddPlaceBox(){
    document.getElementById('addSchedulePlaceBox').classList.add('disappear');
}

const planInfoSelects = document.getElementsByClassName("planInfoRowTitle");
const planInfoRows = document.getElementsByClassName("planInfoRow");
for(let i=0;i<planInfoSelects.length;i++){
    const select = planInfoSelects[i];
    const row = planInfoRows[i];
    select.addEventListener("click",function(e){
        e.preventDefault();
        if(row.classList.contains("detail")){
            row.classList.add("removeDetailAnimation")
            row.addEventListener('animationend', function(){
                row.classList.remove("removeDetailAnimation")
                row.classList.remove("detail");
            });
        }else{
            for(let item of planInfoRows){
                if(item == row){
                    row.classList.add("addDetailAnimation")
                    row.addEventListener('animationend', function(){
                        row.classList.remove("addDetailAnimation")
                        row.classList.add("detail");
                    });
                }else if(item.classList.contains("detail")){
                    item.classList.add("removeDetailAnimation")
                    item.addEventListener('animationend', function(){
                        item.classList.remove("removeDetailAnimation")
                        item.classList.remove("detail");
                    });
                }
            }

        }
    })
}

// 0603
document.getElementById("mateId").addEventListener("keypress",function (e){
    if(e.code=="Enter"){
        searchMate();
    }
})
const inviteMateListBox = document.getElementById("inviteMateListBox");
function eventFunction(){
    const notice = document.querySelector("#inviteMateListBox .notice");
    if(inviteMateListBox.children.length==1){
        notice.classList.remove("disappear");
    }else if(inviteMateListBox.children.length>1){
        notice.classList.add("disappear");
    }
}
function searchMate(){
    const mateId = document.getElementById("mateId").value;
    const inviteMateBoxNotice = document.getElementById("inviteMateBoxNotice");
    inviteMateBoxNotice.innerText = "";
    const searchUserList = document.getElementById("searchUserList");

    if(mateId == ""){
        inviteMateBoxNotice.innerText = "아이디를 입력해주세요";
        return;
    }
    $.ajax({
        url: "/user/searchId",
        data: {
            userid : mateId
        },
        method: "POST",
        dataType: "json",
        success: function (data) {
            searchUserList.innerHTML = "";
            if(data.length==0){
                inviteMateBoxNotice.innerText = "검색 결과가 없습니다";
                return;
            }
            const sampleLi = document.createElement("li");
            const cancelBtn = document.createElement("input");
            cancelBtn.setAttribute("type","button");
            cancelBtn.setAttribute("value","X");
            cancelBtn.classList.add("cancelBtn");
            sampleLi.classList.add("userInfo");
            for(let i = 0;i<data.length;i++){
                const user = data[i];
                const li = sampleLi.cloneNode(true);
                let username = user.username.charAt(0);
                for(let j = 1;j<user.username.length;j++){ username+="*";}
                li.innerHTML = `<img src="/file/thumbnail?systemname=${user.userprofile=='planz'?'default-user.webp':user.userprofile}">
                                    <input type="text" class="userid" readonly value="${user.userid}">
                                    <input type="text" class="username" readonly value="${username}">`;
                li.addEventListener("dblclick",function (e){
                    e.preventDefault();
                    const btn = cancelBtn.cloneNode(true);
                    btn.addEventListener("click",function (e){
                        li.remove();
                        eventFunction();
                    });
                    li.appendChild(btn);
                    li.children[1].setAttribute("name","useridList");
                    inviteMateListBox.appendChild(li);
                    eventFunction();
                })
                searchUserList.appendChild(li);
            }
        },
        error: function (data) {
            console.log("/user/searchId 에러 발생");
        }
    })

}

// 카테고리 선택 이벤트 입력
const bctList = document.getElementsByClassName("bct");
const mctBox = document.getElementById("middleCategoryList");
const mctList  = document.getElementsByClassName("mct");
const sctBox = document.getElementById("smallCategoryList");
const sctList  = document.getElementsByClassName("sct");
// 대카테고리 클릭 이벤트 추가
for(let bct of bctList){
    bct.addEventListener("click",function(e){
        e.preventDefault();
        mctBox.classList.remove("disappear");
        sctBox.classList.add("disappear");
        for(let item of bctList){
            if(item.id == bct.id){
                item.classList.add("select");
            }else{
                item.classList.remove("select");
            }
        }
        for(let mct of mctList){
            if(mct.classList.contains(bct.id)){
                mct.classList.remove("disappear");
            }else{
                mct.classList.add("disappear");
            }
        }
    })
}
// 중카테고리 클릭 이벤트 추가
for(let mct of mctList){
    mct.addEventListener("click",function(e){
        e.preventDefault();
        sctBox.classList.remove("disappear");
        for(let item of mctList){
            if(item.id == mct.id){
                item.classList.add("select");
            }else{
                item.classList.remove("select");
            }
        }
        for(let sct of sctList){
            if(sct.classList.contains(mct.id)){
                sct.classList.remove("disappear");
            }else{
                sct.classList.add("disappear");
            }
        }

    })
}
// 소카테고리 클릭 이벤트 추가
for(let sct of sctList){
    sct.addEventListener("click",function(e){
        e.preventDefault();
        if(!sct.classList.contains("fix")){
            for(let item of sctList){
                if(item.id == sct.id){
                    item.classList.add("select");
                }else{
                    item.classList.remove("select");
                }
            }
            const ulList = document.getElementsByClassName("categoryList");
            const cts = document.querySelectorAll(".categoryBox .select");
            if(cts[0].innerText == "지역"){
                const text = cts[1].innerText + " "+ cts[2].innerText;
                const node = createCategoryNode(text);
                node.classList.add("region");
                ulList[0].innerHTML = "";
                ulList[0].appendChild(node);
            }else if(cts[0].innerText == "음식"){
                if(ulList[1].children.length > 5){
                    alert("음식은 최대 5개까지 선택 가능합니다");
                }else{
                    const text = cts[2].innerText;// + " "+ cts[2].innerText;
                    const node = createCategoryNode(text);
                    node.classList.add("food");
                    sct.classList.add("fix");
                    ulList[1].appendChild(node);
                }
            }else if(cts[0].innerText == "명소"){
                if(ulList[2].children.length > 5){
                    alert("명소는 최대 5개까지 선택 가능합니다");
                    return;
                }else{
                    const text = cts[2].innerText;// + " "+ cts[2].innerText;
                    const node = createCategoryNode(text);
                    node.classList.add("traction");
                    sct.classList.add("fix");
                    ulList[2].appendChild(node);
                }
            }

            for(let ct of cts){ct.classList.remove("select");}
            mctBox.classList.add("disappear");
            sctBox.classList.add("disappear");
        }
    })
}
// 카테고리 노드 생성
function createCategoryNode(text){
    const li = document.createElement("li");
    li.classList.add("selectCT");

    const span = document.createElement("span");
    span.classList.add("categoryText");
    span.innerText = text;
    li.appendChild(span);

    const btn = document.createElement("input");
    btn.setAttribute("type","button");
    btn.setAttribute("value","X");
    btn.classList.add("cancelCTBtn");
    btn.addEventListener("click",function (e){
        e.preventDefault();
        li.remove();
        checkCategoryFix();
    })
    li.appendChild(btn);

    return li;
}

// 케테고리 선태 체크
function checkCategoryFix(){
    const fixCt = document.querySelectorAll(".sct.fix");
    const selectCt = document.querySelectorAll(".categoryText");
    const set = new Set();
    for(let ct of selectCt){
        set.add(ct.innerText);
    }
    for(let ct of fixCt){
        if(!set.has(ct.innerText)){
            ct.classList.remove("fix");
        }
    }
}

const planFormSubmit = document.getElementById("planFormSubmit");
planFormSubmit.addEventListener("click",function (e){
    const plantitle = document.getElementById("plantitle");
    if(plantitle.value === ""||plantitle.value.length<5){
        alert("일정명 5자 이상 입력해주세요");
        return;
    }

    const planmemo = document.getElementById("planmemo");
    if(planmemo.value === ""||planmemo.value.length<10){
        alert("일정내용 10자 이상 입력해주세요");
        return;
    }

    const categoryList = document.querySelectorAll(".categoryText");
    if(categoryList.length === 0){
        alert("카테고리를 하나 이상 선택해 주세요");
        return;
    }
    inputCategoryValue();

    if(startdate.value === ""||enddate.value === ""){
        alert("날짜를 선택해 주세요");
        return;
    }
    startdate.value = startdate.value.replaceAll(".","_");
    enddate.value = enddate.value.replaceAll(".","_");

    const planForm = document.planForm
    planForm.submit();
})

const nextBtn = document.getElementById("nextBtn");
nextBtn.addEventListener("click",function (e){
    e.preventDefault();
    const stepSigns = document.querySelectorAll("#stepbox .stepsign");
    let index;
    for(let i=0;i<stepSigns.length;i++){
        const sign = stepSigns[i];
        if(!sign.classList.contains("btn")){
            index = i;
        }
    }
    showSelectBox(index+1);
})


function inputCategoryValue(){
    const planCategory = document.getElementById("category");
    const region = document.querySelector("#regionCatgory .categoryText");
    const foods = document.querySelectorAll("#foodCatgory .categoryText");
    const tractions = document.querySelectorAll("#tractionCatgory .categoryText");

    if(region.length!=0){
        planCategory.value = "지역|ct|" + region.innerText;
    }

    if(foods.length != 0){
        planCategory.value += "|bct|음식";
        for(let food of foods){
            planCategory.value += "|ct|"+food.innerText;
        }
    }

    if(tractions.length != 0){
        planCategory.value += "|bct|명소";
        for(let traction of tractions){
            planCategory.value += "|ct|"+traction.innerText;
        }
    }
}