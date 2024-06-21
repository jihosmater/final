function getPlanList(startrow){
    const myPlanBox = document.getElementById("myPlanBox");
    const btn = document.getElementById("getPlanBtn");
    btn.classList.add("disappear");
    $.ajax({
        url: "/user/myplan",
        data: {
            startrow: Number(startrow)
        },
        method: "POST",
        dataType: "json",
        success: function (data) {
            const planlist = data.planList;
            const ulistMap = data.userList;

            for(let plan of planlist){
                const plannum = plan.plannum;
                let str = `<a class="plan" href="/plan/get?plannum=${plannum}">
                    <span class="plantitle">${plan.plantitle}</span>
                    <span class="planmemo">${plan.planmemo}</span>
                    <ul class="userList">`;

                const users = ulistMap[plannum];
                // str+=`<li class="user">
                //         <img class="userprofile creater" src="/file/thumbnail?systemname=${users[0].userprofile=='planz'?'default-user.webp':users[0].userprofile}" alt="${users[0].userid}">
                //       </li>`;
                for(let i =0;i<(users.length<5?users.length:4);i++){
                    const user = users[i];
                    str += `<li class="user">
                                <img class="userprofile${i===0?" creater":""}" src="/file/thumbnail?systemname=${user.userprofile=='planz'?'default-user.webp':user.userprofile}" alt="${user.userid}">
                            </li>`;
                }
                if(users.length>4) {
                    str += `<span class="userCnt">외 ${users.length-4}명</span>`;
                }
                // 일정 날짜 문자열 생성
                const start = plan.startdate.split('_');
                const end = plan.enddate.split('_');
                const startdate = start[0]+"년 "+Number(start[1])+"월 "+Number(start[2])+"일"
                const enddate = end[0]+"년 "+Number(end[1])+"월 "+Number(end[2])+"일"

                // 카테고리 생성
                const bcts = plan.category.split('|bct|');

                str += `</ul>
                        <span class="plandate">
                            <span class="startdate">${startdate}</span> ~ <span class="enddate">${enddate}</span>
                        </span>
                        <ul class="planCategory">`;

                for(let bct of bcts){
                    const ct = bct.split('|ct|');
                    for(let i = 1; i<ct.length;i++){
                        str+= `<li class="category">#${ct[i]}</li>`;
                    }
                }
                str += `</ul></a>`;
                myPlanBox.innerHTML += str;
            }
            if(planlist.length === 0){
                btn.remove();
            }else{
                btn.setAttribute("onclick",'getPlanList('+(startrow+8)+')');
                btn.classList.remove("disappear");
            }
        },
        error: function (data) {
            console.log("/user/myplan 에러 발생");
            btn.classList.remove("disappear");
        }
    })
}
getPlanList(0);