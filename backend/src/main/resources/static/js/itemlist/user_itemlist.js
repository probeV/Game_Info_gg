import startResetTimeTimer from './user_itemlist_util.js';

$(document).ready(function() {
    fetchUserItems();
    startResetTimeTimer();
});

// 아이템 시간 초기화 버튼 클릭 이벤트
$(document).on('click', '.btn-reset-time', function() {
    const userItemId = $(this).closest('.item').data('user-item-id');

    const resetTime = new Date()
    resetTime.setDate(resetTime.getDate() + 14);

    const UserItemListUpdateRequestDto = [
        {
            userItemId: userItemId,
            resetTime: resetTime.toISOString().split('.')[0]
        }
    ];

    $.ajax({
        url: '/api/v1/users/items',
        method: 'PUT',
        data: JSON.stringify(UserItemListUpdateRequestDto),
        contentType: 'application/json',
        success: function(response) {
            console.log('내 가방 항목 수정 성공');
            fetchUserItems();
        },
        error: function(error) {
            console.error('Error updating items:', error);
            alert('내 가방 설정 실패');
        }
    });
});

function fetchUserItems() {
    const itemListContainer = $('#items');

    $.get(`/api/v1/users/items`, function(data) {

        itemListContainer.empty(); // 기존 아이템 리스트 초기화

        data.forEach(item => {
            const itemElement = $(`
                <div class="col-12 col-md-6 col-lg-4 mb-2 mt-2">
                    <div class="item" data-user-item-id="${item.userItemId}">
                        <div class="item_area">
                            <img class="item_image" src="${item.imageUrl}" alt="아이템 이미지">
                            <div class="item_info">
                                <div class="item_name_area">
                                    <p class="item_name">${item.name}</p>
                                </div>
                                <p class="item_info_label">남은 시간</p>
                                <p class="item_reset_time" data-reset-time="${item.resetTime}"</p>
                                <div class="btn-reset-time">
                                    <i class="fa-solid fa-arrows-spin fa-2xl" style="color: var(--white-color); padding: 5px;"></i>
                                </div>
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