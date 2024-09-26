$(document).ready(function() {
    // 초기 아이템 리스트 로드
    fetchItems('/api/v1/items');
});

// 검색 버튼 클릭 이벤트
$('.search i').on('click', function() {
    const keyword = $('.search input').val().trim();
    if (keyword) {
        fetchItems(`/api/v1/items/search?keyword=${keyword}`);
    }
    else {
        fetchItems('/api/v1/items');
    }
});

// 엔터 키 이벤트
$('.search input').on('keydown', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // 기본 엔터키 동작 방지
        const keyword = $('.search input').val().trim();
        if (keyword) {
            fetchItems(`/api/v1/items/search?keyword=${keyword}`);
        }
        else {
            fetchItems('/api/v1/items');
        }
    }
});


function fetchItems(url) {
    const itemListContainer = $('#items');

    $.get(url, function(data) {
        itemListContainer.empty(); // 기존 아이템 리스트 초기화
        data.forEach(item => {
            const itemElement = $(`
                <div class="col-12 col-md-6 col-lg-4 mb-3 mt-3">
                    <div class="item">
                        <div class="item_head_area">
                            <img class="item_head_image" src="${item.imageUrl}" alt="아이템 이미지">
                            <div class="item_head_info">
                                <div class="item_head_name_area">
                                    <p class="item_head_name">${item.name}</p>
                                </div>
                                <p class="item_head_effect">${item.effect}</p>
                            </div>
                        </div>
                        <hr>
                        <div class="item_body_area">
                            <div class="item_body_info">
                                <p class="item_body_info_description">${item.description}</p>
                            </div>
                        </div>
                    </div>
                </div>
            `);
            itemListContainer.append(itemElement);
        });
    }).fail(function(error) {
        console.error('Error fetching items:', error);
    });
}