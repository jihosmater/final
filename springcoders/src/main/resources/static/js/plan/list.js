const keywordbox = document.getElementById("keyword");
keywordbox.addEventListener("keypress",function (e){
    if(e.code==="Enter"){
        search();
    }
})

function search(){
    const keyword = keywordbox.value;
    location.replace("http://localhost:8080/plan/list?amount=8&pagenum=1&keyword="+keyword);
}