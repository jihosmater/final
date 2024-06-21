const registerForm = document.registerForm;

function Validation(){
    const re_files = document.getElementById("re_files");
    const re_name = document.getElementById("re_name");
    const re_price_box = document.getElementById("re_price_box");
    const re_itemcontents_box = document.getElementById("re_itemcontents_box");
    const date_box = document.getElementById("date");

    // 보더 색상 초기화
    re_files.style.borderColor = "#faf4f3";
    re_name.style.borderColor = "#faf4f3";
    re_price_box.style.borderColor = "#faf4f3";
    re_itemcontents_box.style.borderColor = "#faf4f3";
    date_box.style.borderColor = "#faf4f3";

    if(i==0){
        re_files.style.borderColor = "#D9D3D2"
        show_rebox("이미지 1개 이상 등록해 주세요");

        re_files.classList.add('shake');

        setTimeout(function() {
            re_files.classList.remove('shake');
        }, 400);
        return false;
    }
    re_files.style.borderColor = "#faf4f3";

    const boardtitle = registerForm.boardtitle;
    if(boardtitle.value == " "){
        re_name.style.borderColor = "#D9D3D2"
        show_rebox("상품명을 입력해 주세요");

        re_name.classList.add('shake');

        setTimeout(function() {
            re_name.classList.remove('shake');
        }, 400);
        return false;
    }

    if(boardtitle.value.length < 2){
        re_name.style.borderColor = "#D9D3D2"
        show_rebox("상품명을 2자 이상 작성해 주세요");

        re_name.classList.add('shake');

        setTimeout(function() {
            re_name.classList.remove('shake');
        }, 400);
        return false;
    }
    re_name.style.borderColor = "#faf4f3";

    const travelPlansStart = document.getElementById("travelPlansStart");
    if(travelPlansStart.value == ""){
        travelPlansStart.style.borderColor = "#D9D3D2";
        show_rebox("시작일자를 입력해주세요");

        travelPlansStart.classList.add('shake');

        setTimeout(function() {
            travelPlansStart.classList.remove('shake');
        }, 400);
        return flase;
    }
    travelPlansStart.style.borderColor = "#faf4f3";

    const travelPlansEnd = document.getElementById("travelPlansEnd");
    if(travelPlansEnd.value == ""){
        travelPlansEnd.style.borderColor = "#D9D3D2";
        show_rebox("종료일자를 입력해주세요");
        travelPlansEnd.classList.add('shake');
        setTimeout(function() {
            travelPlansEnd.classList.remove('shake');
        }, 400);
        return flase;
    }
    travelPlansEnd.style.borderColor = "#faf4f3";

    const price = document.getElementById("price");
    if(price.value == ""){
        re_price_box.style.borderColor = "#D9D3D2";
        show_rebox("가격을 입력해주세요");

        re_price_box.classList.add('shake');

        setTimeout(function() {
            re_price_box.classList.remove('shake');
        }, 400);
        return false;
    }
    re_price_box.style.borderColor = "#faf4f3";

    const boardcontents = document.getElementById("boardcontents");
    if(boardcontents.value == ""){
        re_itemcontents_box.style.borderColor = "#D9D3D2";
        show_rebox("상품에 대한 정보를 작성해주세요");

        re_itemcontents_box.classList.add('shake');

        setTimeout(function() {
            re_itemcontents_box.classList.remove('shake');
        }, 400);
        return false;
    }
    re_itemcontents_box.style.borderColor = "#faf4f3";

    // const donation = document.getElementById("re_donation");
    // if(donation.value == ""){
    //     re_donationbox.style.borderColor = "#D9D3D2"
    //     show_rebox("기부금액을 입력해주세요");
    //     return flase;
    // }
    // re_donationbox.style.borderColor = "#faf4f3"

    return true;
}

function sendit(){
    if(Validation()){
        const price = document.getElementById("price");
        price.value = price.value.replace(/^\D+/,'').replace(/,/g,'');
        console.log(price.value);


        document.getElementById('travelPlansStart').value += " 00:00:00.0";
        document.getElementById('travelPlansEnd').value += " 00:00:00.0";

        // document.getElementById()

        registerForm.submit();
    }
}

function show_rebox(text){
    const re_notice_box = document.getElementById("re_notice_box");
    document.getElementById("re_notice_text").innerText = text;
    re_notice_box.setAttribute("style","font-weight:bold; display:'inline-block'; animation: re_notice 1.5s ease-in-out forwards;");
    re_notice_box.addEventListener('animationend', (event) => {
        re_notice_box.style.display = "none";
        re_notice_box.style.animation = "";
    });
}


// 파일 업로드 부분 시작
let i = 0; // 현재 item사진 수
function addfile() {
    if (i == 10) {
        alert("사진은 최대 10장까지만 가능합니다.")
    } else {
        $("#file" + i).click();
        $("[type=file]").change(function (e) {
            const fileTag = e.target;
            console.log(fileTag);
            const file = fileTag.files[0];
            console.log(file);

            if (file == undefined) {
                //업로드 창을 띄웠다가 취소한 경우(파일이 업로드 되었다가 없어진 경우)
                deletefile(i);

            }
            else {
                const reader = new FileReader();
                let url;
                reader.onload = function (ie) {
                    // console.log(ie.target.result)
                    url = ie.target.result;
                    const tdTag = document.getElementById("f" + i);
                    tdTag.style = "background: url(" + url + ") no-repeat center/100%;";
                    i++;
                    document.getElementById("filecnt").innerText = i;

                    const td = document.createElement("td");
                    td.setAttribute("class","file");
                    td.setAttribute("id","f"+i);
                    td.setAttribute("style","display:none;");

                    const inputFile = document.createElement("input");
                    inputFile.setAttribute("type","file");
                    inputFile.setAttribute("name","files");
                    inputFile.setAttribute("id","file"+i);
                    inputFile.setAttribute("accept","image/gif, image/jpeg, image/png");
                    inputFile.setAttribute("style","display:none;");

                    const inputBtn = document.createElement("input");
                    inputBtn.setAttribute("type","button");
                    inputBtn.setAttribute("onclick","deletefile("+i+")");
                    inputBtn.setAttribute("value","X");
                    inputBtn.setAttribute("style","cursor: pointer;");

                    td.appendChild(inputFile);
                    td.appendChild(inputBtn);
                    console.log(td);
                    document.getElementById("re_files").appendChild(td);
                }
                reader.readAsDataURL(file);

            }
        })
    }
}

function deletefile(num) {
    num = Number(num);
    $("#f" + num).remove();
    //지워진 다음 행 부터 숫자 바꿔주기
    for (let j = num + 1; j <= i; j++) {
        console.log(j);
        const el = $("#f" + j);
        el.attr("id", "f" + (j - 1));
        el.find("input[type='file']").attr("name", "file" + (j - 1));
        el.find("input[type='file']").attr("id", "file" + (j - 1));
        el.find("input[type='button']").attr("onclick", "deletefile(" + (j - 1) + ")");
    }
    i--;
    document.getElementById("filecnt").innerText = i;
}

//판매가격 입력 ', '생성 시작
function comma(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
}

function uncomma(str) {
    str = String(str);
    return str.replace(/[^\d]+/g, '');
}

function inputNumberFormat(obj) {
    const price = comma(uncomma(obj.value));
    if(price==0){
        obj.value = "";
    }else{
        obj.value = "₩"+comma(uncomma(obj.value));
    }
}

function inputOnlyNumberFormat(obj) {
    obj.value = onlynumber(uncomma(obj.value));
}

function onlynumber(str) {
    str = String(str);
    return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1');
}
//판매가격 입력 ', '생성 끝

function itemconditionwrite(txt){
    document.getElementById("itemcondition").value = txt;
    const btns = document.getElementsByClassName("itemcondition_check");
    for(let btn of btns){
        if(btn.value == txt){
            btn.style.border= "1px solid #00c88c";
        }else{
            btn.style.border = "1px solid #ccc";
        }
    }
}

function freecheck(txt){
    const freecheckbtn = document.getElementById("freecheckbtn");
    const price_box = document.getElementById("price_box");
    const price = document.createElement("input");
    const donation = document.getElementById("re_donation");

    if(txt=='X'){
        freecheckbtn.style.border = "1px solid #00c88c";
        document.getElementById("price").remove();
        price.setAttribute("type","text");
        price.setAttribute("id","price");
        price.setAttribute("name","price");
        price.setAttribute("readonly","readonly");
        price.setAttribute("value","무료나눔");
        price.setAttribute("style","cursor: default;");
        freecheckbtn.setAttribute("onclick","freecheck('O')");
        donation.value = 3000;
        donation.setAttribute("readonly","readonly");
    }else{
        freecheckbtn.style.backgroundColor = "";
        document.getElementById("price").remove();
        price.setAttribute("type","text");
        price.setAttribute("id","price");
        price.setAttribute("name","price");
        price.setAttribute("maxlength","10");
        price.setAttribute("onkeyup","inputNumberFormat(this)");
        price.setAttribute("placeholder","판매가격");
        freecheckbtn.setAttribute("onclick","freecheck('X')");
        donation.value = "";
        donation.removeAttribute("readonly");
    }
    price_box.appendChild(price);
}
$(document).ready(function() {
    $.datepicker.setDefaults($.datepicker.regional['ko']);

    var startDateTextBox = $('#travelPlansStart');
    var endDateTextBox = $('#travelPlansEnd');
    var datepickerContainer = $('.datepicker-container');

    function showDatepicker() {
        datepickerContainer.show();
        datepickerContainer.find('.ui-datepicker').datepicker('show');
    }

    function hideDatepicker() {
        datepickerContainer.hide();
    }

    // Datepicker for start date
    startDateTextBox.datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(selectedDate) {
            endDateTextBox.datepicker("option", "minDate", selectedDate);
            hideDatepicker();
        }
    });

    // Datepicker for end date
    endDateTextBox.datepicker({
        dateFormat: 'yy-mm-dd',
        onSelect: function(selectedDate) {
            startDateTextBox.datepicker("option", "maxDate", selectedDate);
            hideDatepicker();
        }
    });

    // Open datepicker when calendar div is clicked
    $('#calendar').click(function() {
        showDatepicker();
    });

    // Set selected date to start date when calendar is clicked
    $('#calendar').click(function() {
        startDateTextBox.datepicker('show');
    });

    // Reset button click event
    $('#reset_button').click(function() {
        startDateTextBox.val('');
        endDateTextBox.val('');
        // Remove minDate option for endDatePicker
        endDateTextBox.datepicker("option", "minDate", null);
        // Remove maxDate option for startDatePicker
        startDateTextBox.datepicker("option", "maxDate", null);
    });
});