const arCategory = [];

function sendit() {
	const joinForm = document.joinForm;
	const userid = joinForm.userid;
	if (userid.value == "") {
		alert("아이디를 입력하세요!");
		userid.focus();
		return;
	}
	if (userid.value.length < 5 || userid.value.length > 12) {
		alert("아이디는 5자 이상 12자 이하로 입력하세요!");
		userid.focus();
		return;
	}

	if (result.innerHTML == "") {
		alert("아이디 중복검사를 진행해주세요!");
		userid.focus();
		return;
	}
	if (result.innerHTML == "중복된 아이디가 있습니다!") {
		alert("중복체크 통과 후 가입이 가능합니다!");
		userid.focus();
		return;
	}

	//아래쪽의 pwcheck() 함수를 통해 유효성 검사를 통과했다면 pwTest 배열에는 true값만 존재한다.
	//무언가 실패했다면 false가 포함되어 있으므로, 반복문을 통해 해당 배열을 보며 false값이 있는지 검사

	/*for (let i = 0; i < 5; i++) {
		if (!pwTest[i]) {
			alert("비밀번호 확인을 다시하세요!");
			password.value = "";
			password.focus();
			return;
		}
	}*/
	const userpwch = joinForm.passwordcheck;
	const userpw = joinForm.userpw;
	const username = joinForm.username;
	const userphone = joinForm.userphone;
	const exp_number = /^[0-9]+$/;
	const exp_name = /^[가-힣a-zA-Z]+$/;
	const exp_password = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#])[\da-zA-Z!@#]{8,}$/;


	if (userpw.value === "") {
		alert("비밀번호를 입력해주세요!");
		return;
	}
	if (userpw.value.length < 5 || userpw.value.length > 12) {
		alert("비밀번호는 5자 이상 12자 이하로 입력하세요!");
		return;
	}
	if (!exp_password.test(userpw.value)) {
		alert("비밀번호에 숫자와 알파벳 대, 소문자가 포함되어있나요?");
		return;
	}
	if (userpw.value !== userpwch.value) {
		alert("비밀번호가 일치하지않아요!");
		return;
	}

	if (!exp_name.test(username.value)) {
		alert("이름에는 한글과 영어만 입력 할 수 있어요!");
		username.focus();
		return false;
	}
	if (userphone.value === ""){
        alert("전화번호를 입력해주세요!");
        userphone.focus();
        return;
    }
	if (!exp_number.test(userphone.value)) {
		alert("전화번호에는 숫자만 입력해주세요!!");
		userphone.focus();
		return;
	}
	if (userphone.value.length < 9 || userphone.value.length > 12) {
		alert("전화번호를 제대로 입력하신건가요?");
		userphone.focus();
		return;
	}

	/* const usergender = joinForm.usergender;
	 if(!usergender[0].checked && !usergender[1].checked){
		 alert("성별을 선택하세요!");
		 return;
	 }*/

	const zipcode = joinForm.zipcode;
	if (zipcode.value == "") {
		alert("주소찾기를 진행해 주세요!");
		findAddr();
		return;
	}
	const addrdetail = joinForm.addrdetail;
	if (addrdetail.value == "") {
		alert("상세주소를 입력해 주세요!");
		addrdetail.focus();
		return;
	}

	if (arCategory.length == 0) {
		alert("취미는 적어도 1개 이상 입력해 주세요!");
		return;
	}

	const CategoryTag = joinForm.Category;
	CategoryTag.value = arCategory.join("\\");// "축구\농구\영화";
	console.log("회원가입 성공");
	alert("회원가입에 성공하였습니다!");
	joinForm.submit();
}

document.querySelectorAll('a[href^="#"]').forEach(anchor => {
	anchor.addEventListener('click', function(e) {
		// Prevent the default action
		e.preventDefault();

		// Remove the highlight class from all sections
		// document.querySelectorAll('h2').forEach(section => {
		//     section.classList.remove('highlight');
		// });

		// Get the target element
		const targetId = this.getAttribute('href').substring(1);
		const targetElement = document.getElementById(targetId);

		// Scroll to the target element
		targetElement.scrollIntoView({ behavior: 'smooth' });

		// Add highlight class to the target element
		// targetElement.classList.add('highlight');
	});
});

function sendCategoryId(CategoryId) {
	window.opener.document.getElementById("receive_Category").value = CategoryId;
	window.close();
}

function createCategory() {
	window.open("/user/JoinCategory", "Category", "width=750,height=500,left=100,top=50");
}

function addCategory() {
	const joinForm = document.joinForm;
	const Category_list = document.getElementsByClassName("Category_list")[0];
	const receive_Category = joinForm.receive_Category;

	if (receive_Category.value == "") {
		alert("취미를 입력해 주세요!");
		receive_Category.focus();
		return;
	}
	if (arCategory.indexOf(receive_Category.value) != -1) {
		alert("중복된 취미입니다!");
		receive_Category.focus();
		receive_Category.value = "";
		return;
	}
	if (arCategory.length == 5) {
		alert("취미는 5개 이하로 입력해주세요!");
		return;
	}

	// span 태그 노드 생성
	const inputCategory = document.createElement("span");
	// span 태그 노드 클래스 값으로 userCategory
	inputCategory.classList = "userCategory";
	// span 태그 노드 name 값으로 userCategory
	inputCategory.name = "userCategory";
	inputCategory.id = "userCategory";
	// span 태그 노드 내부 내용으로 입력한 취미 문자열 설정
	inputCategory.innerHTML = receive_Category.value;
	// 취미 목록 배열에 입력한 취미 문자열 추가
	arCategory.push(receive_Category.value);

	// span 태그 클릭 시 삭제 이벤트 설정
	inputCategory.addEventListener('click', deleteCategory);

	// 아래쪽의 취미 목록을 보여줄 div의 자식으로 span태그 추가
	Category_list.appendChild(inputCategory);

	receive_Category.value = "";
	receive_Category.focus();

	function deleteCategory(event) {
		// 클릭한 span 태그를 삭제
		Category_list.removeChild(event.target);

		// 삭제된 취미 문자열을 배열에서도 제거
		const index = arCategory.indexOf(event.target.innerHTML);
		if (index > -1) {
			arCategory.splice(index, 1);
		}
	}

}

function findAddr() {
	new daum.Postcode({
		oncomplete: function(data) {
			// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

			// 각 주소의 노출 규칙에 따라 주소를 조합한다.
			// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
			var addr = ''; // 주소 변수
			var extraAddr = ''; // 참고항목 변수

			//사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
			if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
				addr = data.roadAddress;
			} else { // 사용자가 지번 주소를 선택했을 경우(J)
				addr = data.jibunAddress;
			}

			// 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.

			// 우편번호와 주소 정보를 해당 필드에 넣는다.
			document.getElementById('jo_zipcode').value = data.zonecode;
			document.getElementById("jo_addr").value = addr;
			// 커서를 상세주소 필드로 이동한다.
			document.getElementById("jo_addrdetail").focus();
		}
	}).open();
}
function checkId() {
	const xhr = new XMLHttpRequest();
	const userid = document.joinForm.userid;
	console.log("ㅋㅋㅋ")

	if (userid.value == "") {
		alert("아이디를 입력하세요!");
		userid.focus();
		return;
	}

	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				let txt = xhr.responseText.trim();
				if (txt == "O") {
					result.innerHTML = "중복된 아이디가 있습니다!";
					userid.value = "";
					userid.focus();
				}
				else {
					result.innerHTML = "사용할 수 있는 아이디입니다!";
					document.joinForm.password.focus();
				}
			}
		}
	}

	xhr.open("GET", "/user/checkId?userid=" + userid.value);
	xhr.send();
}
function checkPN() {
	const xhr = new XMLHttpRequest();
	const exp_number = /^[0-9]+$/;
	const userphone = document.joinForm.userphone;
	console.log("ㅋㅋㅋ")

	if (userphone.value == "") {
		alert("전화번호를 입력하세요!");
		userphone.focus();
		return;
	}

	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				let txt = xhr.responseText.trim();
				if (txt == "O") {
					result.innerHTML = "이미 가입된 전화번호입니다!";
					userphone.value = "";
					userphone.focus();
				}
				else {
					if (!exp_number.test(userphone.value)) {
						result.innerHTML = "전화번호에는 숫자만 입력해주세요!!";
						userphone.focus();
						return;
					}
					if (userphone.value.length < 9 || userphone.value.length > 12) {
						result.innerHTML = "전화번호를 제대로 입력하신건가요?";
						userphone.focus();
						return;
					}
					result.innerHTML = "사용할 수 있는 전화번호입니다!";
				}
			}
		}
	}

	xhr.open("GET", "/user/checkPN?userphone=" + userphone.value);
	xhr.send();
}
function checkemail() {
	const xhr = new XMLHttpRequest();
	const useremail = document.joinForm.useremail;

	if (useremail.value == "") {
		alert("이메일을 입력하세요!");
		useremail.focus();
		return;
	}

	xhr.onreadystatechange = function() {
		if (xhr.readyState == 4) {
			if (xhr.status == 200) {
				let txt = xhr.responseText.trim();
				console.log("아니 이게 왜됨")
				if (txt == "O") {
					result.innerHTML = "이미 있는 이메일입니다!";
					useremail.value = "";
					useremail.focus();
				}
				else {
					result.innerHTML = "사용할 수 있는 이메일입니다!";
				}
			}
		}
	}

	xhr.open("GET", "/user/checkemail?useremail=" + useremail.value);
	xhr.send();
}
function changeGender() {
	const btn = document.getElementById("innerDiv1");
	const gender = document.getElementById("gender");
	console.log(gender);
	if (btn.innerText == "여성") {
		btn.setAttribute("style", "animation: moverightbtn 0.2s ease-in-out forwards;");
		btn.addEventListener('animationend', (event) => {
			btn.style.left = "27px";
			btn.innerText = "남성";
			gender.setAttribute("value", "남성");
			// gender.value = "남성";
		});
	}
	else if (btn.innerText == "남성") {
		btn.setAttribute("style", "animation: moveleftbtn 0.2s ease-in-out forwards;");
		btn.addEventListener('animationend', (event) => {
			btn.style.left = "2px";
			btn.innerText = "여성";
			gender.setAttribute("value", "여성");
			// gender.value = "여성";
		});
	}
}
function deleteCategory(e) {
	let deleteNode = null;
	if (e.target.classList == "xBox") {
		deleteNode = e.target.parentNode;
	}
	else {
		deleteNode = e.target;
	}

	let txt = deleteNode.innerText;
	console.log(txt);
	for (let i in arCategory) {
		if (arCategory[i] == txt) {
			arCategory.splice(i, 1);
			break;
		}
	}

	deleteNode.remove();
}
function Loginsendit() {
    var loginuserid = document.getElementById('loginuserid');
    var loginuserpw = document.getElementById('loginuserpw');
    var show_box = document.getElementById('show_box');

    if (loginuserid.value === "") {
        show_box.innerText = "※ 아이디를 입력하세요";
        show_box.style.fontWeight = "bold";
        loginuserid.focus();
        return;
    }

    if (loginuserpw.value === "") {
        show_box.innerText = "※ 비밀번호를 입력하세요";
        show_box.style.fontWeight = "bold";
        loginuserpw.focus();
        return;
    }

    // Ajax 요청
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                console.log("신호 들어가기 후");
                var response = xhr.responseText.trim();
                console.log(response);
                if (response !== "") {
                    alert("Planz에 오신 걸 환영합니다!");
                    window.location.href = "/"; // 로그인 성공 시 홈 페이지로 이동
                } else {
                    console.log("안왔는데");
                    alert("아이디 또는 비밀번호가 틀렸습니다");
                    loginuserpw.focus();
                }
            }
        }
    };

    var encodedId = encodeURIComponent(loginuserid.value);
    var encodedPw = encodeURIComponent(loginuserpw.value);
    xhr.open("POST", "/user/login", true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.send("loginuserid=" + encodedId + "&loginuserpw=" + encodedPw);
}
