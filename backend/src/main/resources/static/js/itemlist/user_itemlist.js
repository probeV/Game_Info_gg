import startResetTimeTimer from './user_itemlist_util.js';


$(document).ready(function() {
    fetchItems();
    startResetTimeTimer();
});

function fetchItems() {
    const itemListContainer = $('#items');

    $.get(`/api/v1/users/items`, function(data) {

        itemListContainer.empty(); // 기존 아이템 리스트 초기화

        data.forEach(item => {
            const itemElement = $(`
                <div class="col-12 col-md-6 col-lg-4 mb-2 mt-2">
                    <div class="item">
                        <div class="item_area">
                            <img class="item_image" src="${item.imageUrl}" alt="아이템 이미지">
                            <div class="item_info">
                                <div class="item_name_area">
                                    <p class="item_name">${item.name}</p>
                                </div>
                                <p class="item_info_label">남은 시간</p>
                                <p class="item_reset_time" data-reset-time="${item.resetTime}"</p>
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