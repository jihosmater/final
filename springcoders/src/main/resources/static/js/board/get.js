var elements = document.getElementsByClassName("comma");

// 요소들을 순회하면서 가격을 형식화합니다.
for (var i = 0; i < elements.length; i++) {
    var price = elements[i].textContent; // 요소의 텍스트 내용을 가져옵니다.
    price = parseInt(price.replace(/,/g, "")); // 쉼표를 제거하고 숫자로 변환합니다.
    price = price.toLocaleString(); // 3자리마다 쉼표를 추가합니다.
    elements[i].textContent = price + "원"; // 형식화된 가격에 "원"을 추가하여 요소의 텍스트를 업데이트합니다.
}

document.addEventListener('DOMContentLoaded', (event) => {
    const imgUl = document.getElementById('img_ul');
    const imgLis = imgUl.getElementsByTagName('li');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    let currentIndex = 0;

    function updateVisibility() {
        for (let i = 0; i < imgLis.length; i++) {
            if (i === currentIndex) {
                imgLis[i].classList.add('active');
            } else {
                imgLis[i].classList.remove('active');
            }
        }
    }

    prevBtn.addEventListener('click', () => {
        currentIndex = (currentIndex === 0) ? imgLis.length - 1 : currentIndex - 1;
        updateVisibility();
    });

    nextBtn.addEventListener('click', () => {
        currentIndex = (currentIndex === imgLis.length - 1) ? 0 : currentIndex + 1;
        updateVisibility();
    });

    // Initialize the visibility
    updateVisibility();
});


