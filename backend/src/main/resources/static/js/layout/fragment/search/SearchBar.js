// 키워드 입력 후 엔터 이벤트
export function searchEnterEvent(callback){
    $('.search_input').on('keydown', function(event) {
        if (event.key === 'Enter') {
            event.preventDefault(); // 기본 엔터키 동작 방지
            const keyword = $(this).val().trim();
            callback(keyword);
        }
    });
    
}

// 키워드 입력 후 검색 버튼 클릭 이벤트
export function searchIconClickEvent(callback){
    $('.search_icon').on('click', function() {
        const keyword = $('.search_input').val().trim();
        callback(keyword);
    });
}