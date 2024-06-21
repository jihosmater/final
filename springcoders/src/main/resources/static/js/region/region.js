let eventlist_;
let staylist;
let courselist;
let spotlist;
let foodlist;

var calendar;
const nowDate = new Date();
let date = [nowDate.getFullYear(), nowDate.getMonth() + 1];

function doing(areaCode, option) {

    showLoding();
    $.ajax({
        url: "/api/get",
        method: "POST",
        data: {
            arrange: "O",
            areaCode: areaCode,
            order: "area",
        },
        success: function (data) {

            // 1. 지역행사 json 데이터
            const eventData = JSON.parse(data[0]);
            console.log("지역행사");
            eventlist_ = eventData.response.body.items;
            console.log(eventlist_);
            calendar.removeAllEventSources();
            event_list = eventlist_.item
            addEventsToCalendar(event_list);

            // 2. 숙박 json 데이터
            const eventStay = JSON.parse(data[1]);
            console.log("숙박업소");
            staylist = eventStay.response.body.items;
            console.log(staylist);

            // 3. 여행코스 json 데이터
            const eventCourse = JSON.parse(data[2]);
            console.log("여행코스");
            courselist = eventCourse.response.body.items;
            console.log(courselist);

            // 4. 추천 여행지 json 데이터
            const eventSpot = JSON.parse(data[3]);
            console.log("추천 여행지");
            spotlist = eventSpot.response.body.items;
            console.log(spotlist);
            hotSpot(spotlist);
            flowImg(spotlist);


            // 5. 추천 맛집 json 데이터
            const Food = JSON.parse(data[4]);
            console.log("추천 맛집");
            foodlist = Food.response.body.items;
            console.log(foodlist);
            hotFood(foodlist);

            // 6. 키워드 json 데이터
            // const Keyword = JSON.parse(data[5]);
            // console.log("키워드");
            // keywordlist = Keyword.response.body.items;
            // console.log(keywordlist);


        },
        error: function (data) {
            console.log("실패");
        },
    });


    let select_list = document.getElementById("location");
    let text = select_list.options[select_list.selectedIndex].text;
    console.log(text);
    document.getElementById("recommend_local").innerText = text;
    document.getElementById("food_local").innerText = text;
    // document.getElementById("list_local").innerText = text;
    document.getElementById("logo_local").innerText = text;
    document.getElementById("switch_local").innerText = text;
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



// 캘린더 - 지역별 행사 
var usedColors = []; // 이미 선택된 색상을 저장할 배열

function getRandomColor() {
    var colors = ["#26355D", "#344C64", "#577B8D",
        "#DAD3BE"];

    // 사용 가능한 색상을 추림
    var availableColors = colors.filter(function(color) {
        return !usedColors.includes(color);
    });

    // 사용 가능한 색상이 없으면 모든 색상을 재사용
    if (availableColors.length === 0) {
        usedColors = [];
        availableColors = colors;
    }

    var randomIndex = Math.floor(Math.random() * availableColors.length);
    var selectedColor = availableColors[randomIndex];

    // 선택된 색상을 사용된 색상에 추가
    usedColors.push(selectedColor);

    return selectedColor;
}

document.addEventListener("DOMContentLoaded", function (event_list) {
    var calendarEl = document.getElementById("calendar");
    calendar = new FullCalendar.Calendar(calendarEl, {
        timeZone: "UTC",
        initialView: "dayGridMonth",
        events: [{}],
        editable: true,
        eventClick: function (info) {
            var event = info.event;
            $("#eventTitle").text(event.title);
            $("#eventDate").text(event.start.toLocaleDateString());
            $("#eventAddr1").text(event.extendedProps.addr1);
            $("#eventTel").text(event.extendedProps.tel);
            $("#eventImage").attr("src", event.extendedProps.imageUrl);
            $("#eventModal").fadeIn();
        },
    });

    calendar.render();

    var initialEvents = calendar.getEvents(); // 초기 이벤트 저장

    $("#location").change(function () {
        var selectedLocation = $(this).val();
        date = [nowDate.getFullYear(), nowDate.getMonth() + 1];
        doing(selectedLocation);
        if (selectedLocation === "all") {
            calendar.removeAllEventSources();
            calendar.addEventSource(initialEvents); // 초기 이벤트를 다시 추가
        } else {
            var filteredEvents = initialEvents.filter(function (event) {
                return event.extendedProps.location === selectedLocation;
            });
            calendar.removeAllEventSources();
            calendar.addEventSource([{}]);
        }
    });

    $("#closeEventModal").click(function () {
        $("#eventModal").hide();
    });

    $(window).click(function (e) {
        if (e.target == document.getElementById("eventModal")) {
            $("#eventModal").hide();
        }
    });
});
function addEventsToCalendar(events) {
    // 기존 이벤트를 모두 제거
    calendar.removeAllEvents();

    // 이벤트를 추가
    events.slice(0, 5).forEach(function (event, index) {
        calendar.addEvent({
            title: event.title,
            start: event.eventstartdate,
            end: event.eventenddate,
            addr1: event.addr1,
            tel: event.tel,
            imageUrl: event.firstimage,
            color: getRandomColor(),
            location: event.areacode, // 서울 지역 지정
            extendedProps: {
                index: index, // 이벤트의 인덱스 저장
            },
        });
    });

    // 10개 이상의 이벤트가 있을 경우 "..." 이벤트 추가
    // if (events.length > 5) {
    //     calendar.addEvent({
    //         title: "...",
    //         start: new Date(),
    //         allDay: true,
    //         display: "background",
    //         color: "#ccc",
    //     });
    // }
}

function showMoreEvents(events) {
    // "..." 이벤트를 제거
    calendar.getEvents().forEach(function (event) {
        if (event.title === "...") {
            event.remove();
        }
    });

    // 나머지 이벤트를 추가
    events.slice(10).forEach(function (event, index) {
        calendar.addEvent({
            title: event.title,
            start: event.eventstartdate,
            end: event.eventenddate,
            addr1: event.addr1,
            tel: event.tel,
            imageUrl: event.firstimage,
            color: getRandomColor(),
            location: event.areacode, // 서울 지역 지정
            extendedProps: {
                index: index + 10, // 10번째부터 인덱스를 증가시킴
            },
        });
    });


    // X 버튼 클릭 시 나타난 이벤트들을 제거
    $("#closeEventModal").on("click", function () {
        addEventsToCalendar(events);
    });
}

document.addEventListener("DOMContentLoaded", function () {
    $("#calendar").on("click", ".fc-more", function () {
        var events = $(this).data("event")._def.extendedProps.events;
        showMoreEvents(events);
    });
});

document.addEventListener("DOMContentLoaded", function () {
    doing(1);
});

function setLocal(areaCode) {
    document.getElementById("location").value = areaCode;
    doing(areaCode);


    let homepageLink;
    switch (areaCode) {
        case 1: // 서울
            homepageLink = "https://www.sto.or.kr/index";
            break;
        case 2: // 인천
            homepageLink = "https://itour.incheon.go.kr/";
            break;
        case 3: // 대전
            homepageLink = "https://www.djto.kr/kor/index.do";
            break;
        case 4: // 대구
            homepageLink = "https://tour.daegu.go.kr/";
            break;
        case 5: // 광주
            homepageLink = "https://www.gjto.or.kr/kor";
            break;
        case 6: // 부산
            homepageLink = "https://bto.or.kr/kor/Main.do";
            break;
        case 7: // 울산
            homepageLink = "https://www.uto.or.kr/uto/index.do";
            break;
        case 8: // 세종
            homepageLink = "https://www.sejong.go.kr/tour/index.do";
            break;
        case 31: // 경기
            homepageLink = "https://ggtour.or.kr/gto/";
            break;
        case 32: // 강원
            homepageLink = "http://www.gwto.or.kr/www/index.do";
            break;
        case 39: // 제주
            homepageLink = "https://ijto.or.kr/korean/";
            break;
        case 38: // 전남
            homepageLink = "http://ijnto.or.kr/";
            break;
        case 37: // 전북
            homepageLink = "https://www.jbct.or.kr/c_index.php";
            break;
        case 33: // 충북
            homepageLink = "https://tour.chungbuk.go.kr/www/index.do";
            break;
        case 34: // 충남
            homepageLink = "https://www.cacf.or.kr/site/index.php";
            break;
        case 36: // 경남
            homepageLink = "https://gnto.or.kr/main/main.php";
            break;
        case 35: // 경북
            homepageLink = "https://tour.gb.go.kr/";
            break;
        default:
            homepageLink = "https://www.sto.or.kr/index"; // 기본값 설정
    }
    document.getElementById("homepage_local").setAttribute("href", homepageLink);


}
//  추천 숙박에 데이터 값 넣어주기 시작


function hotFood(foodlist) {
    if (!Array.isArray(foodlist.item)) {
        console.error("foodlist 배열이어야 합니다.");
        return;
    }

    // staylist에서 랜덤으로 4개의 항목 선택
    const selectedStays = getRandomElements(foodlist.item, 4);

    // 선택된 각 항목에 대해 처리
    selectedStays.forEach((stay, index) => {
        $("#stay_title" + (index + 1)).text(stay.title);
        $("#stay_img" + (index + 1)).css(
            "background-image",
            "url(" + stay.firstimage + ")"
        );
        $("#stay_addr" + (index + 1)).text(stay.addr1);
        $("#food" + (index + 1)).attr("onclick", "get("+stay.contentid+","+stay.contenttypeid+")");
        console.log(stay.contenttypeid);
    });
}

// 배열에서 랜덤하게 n개의 요소를 선택하는 함수
function getRandomElements(array, n) {
    const shuffled = array.slice(); // 배열을 복사하여 새로운 배열 생성
    let i = array.length;
    let temp, randIndex;
    while (i--) {
        randIndex = Math.floor(Math.random() * (i + 1)); // 0부터 i까지의 랜덤한 인덱스
        temp = shuffled[randIndex];
        shuffled[randIndex] = shuffled[i];
        shuffled[i] = temp;
    }
    return shuffled.slice(0, n); // 앞에서부터 n개의 요소 반환
}

// 추천 숙박에 데이터 값 넣어주기 끝

//  추천 여행지에 데이터 값 넣어주기 시작


function hotSpot(spotlist) {
    if (!Array.isArray(spotlist.item)) {
        console.error("spotlist는 배열이어야 합니다.");
        return;
    }

    // spotlist에서 랜덤으로 4개의 항목 선택
    const selectedSpots = getRandomElements(spotlist.item, 4);

    // 선택된 각 항목에 대해 처리
    selectedSpots.forEach((spot, index) => {
        $("#recommend_title" + (index + 1)).text(spot.title);
        $("#recommend_img" + (index + 1)).css(
            "background-image",
            "url(" + spot.firstimage + ")"
        );
        $("#recommend_addr" + (index + 1)).text(spot.addr1);
        $("#spot" + (index + 1)).attr("onclick", "get("+spot.contentid+","+spot.contenttypeid+")");
    });
}

// 배열에서 랜덤하게 n개의 요소를 선택하는 함수
function getRandomElements(array, n) {
    const shuffled = array.slice(); // 배열을 복사하여 새로운 배열 생성
    let i = array.length;
    let temp, randIndex;
    while (i--) {
        randIndex = Math.floor(Math.random() * (i + 1)); // 0부터 i까지의 랜덤한 인덱스
        temp = shuffled[randIndex];
        shuffled[randIndex] = shuffled[i];
        shuffled[i] = temp;
    }
    return shuffled.slice(0, n); // 앞에서부터 n개의 요소 반환
}

function get(contentId,contenttypeid){
    console.log(contentId);
    $("#contendIdValue").attr('value',contentId);
    $("#contentTypeIdValue").attr('value',contenttypeid);



    // regionGetForm을 제출
    $("#regionGetForm").submit();

}

// function hotSpot(hotlist) {
//   for (let i = 0; i < 4; i++) {
//     let item = hotlist[i];
//     if (item) {
//       $("#stay_title" + (i + 1)).text(item.title);
//       $("#stay_img" + (i + 1)).css(
//               "background-image",
//               "url(" + item.firstimage + ")"
//       );
//       $("#stay_addr" + (i + 1)).text(item.addr1);
//     }
//   }
// }

// 현재 슬라이드의 인덱스를 추적하는 변수
var currentSlideIndex = 0;

// 이전 버튼 클릭 시 호출되는 함수
function movePrev() {
    if (currentSlideIndex > 0) {
        currentSlideIndex--;
        updateSlidePosition();
        updateControls();
    }
}

// 다음 버튼 클릭 시 호출되는 함수
function moveNext() {
    var totalSlides = document.querySelectorAll('.slides li').length;
    if (currentSlideIndex < totalSlides - 1) {
        currentSlideIndex++;
        updateSlidePosition();
        updateControls();
    }
}

// 현재 슬라이드 위치 업데이트 함수
function updateSlidePosition() {
    var slideWidth = 120; // 각 슬라이드의 가로 너비
    var newPosition = -slideWidth * currentSlideIndex;
    document.querySelector('.slides').style.transform = 'translateX(' + newPosition + 'px)';
}

// 이전/다음 버튼 상태 업데이트 함수
function updateControls() {
    var prevButton = document.querySelector('.prev');
    var nextButton = document.querySelector('.next');
    var totalSlides = document.querySelectorAll('.slides li').length;

    if (currentSlideIndex === 0) {
        prevButton.style.display = 'none';
    } else {
        prevButton.style.display = 'inline';
    }

    // 현재 슬라이드가 마지막 슬라이드인 경우에만 다음 버튼 숨김
    if (currentSlideIndex === totalSlides - 8) {
        nextButton.style.display = 'none';
    } else {
        nextButton.style.display = 'inline';
    }
}

// 페이지 로드 시 초기화
updateControls();

function flowImg(spotlist){
    if (!Array.isArray(spotlist.item)) {
        console.error("spotlist는 배열이어야 합니다.");
        return;
    }
    console.log("여기 = "+spotlist);
    const selectedSpots = getRandomElements(spotlist.item, 16);
    let str = "";

    selectedSpots.forEach((spot, index) => {
        if (index < 16) { // 최대 8개의 이미지만 가져오기
            str += `<div class="test_img">
                       <img src="${spot.firstimage}" alt="Image ${index + 1}" />
                    </div>`;
        }
    });
    $("#testbox .test").html(str);
}