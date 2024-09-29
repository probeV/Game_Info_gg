$(document).ready(function() {
    fetchUserItemSetting();
    fetchItems('/api/v1/items');
});

// 아이템 삭제 버튼 클릭 이벤트
$(document).on('click', '.btn-remove', function() {
    const itemElement = $(this).closest('.col-12.col-md-6.col-lg-4.mb-2.mt-2');
    itemElement.remove();
});

// 아이템 추가 버튼 클릭 이벤트
$(document).on('click', '.btn-add', function() {
    const itemId = $(this).closest('.item').data('item-id');
    const itemImageUrl = $(this).closest('.item').find('.item_head_image').attr('src');
    const itemName = $(this).closest('.item').find('.item_head_name').text();

    const itemElement = $(`
        <div class="col-12 col-md-6 col-lg-4 mb-2 mt-2">
            <div class="item" data-item-id="${itemId}">
                <div class="item_area">
                    <img class="item_image" src="${itemImageUrl}" alt="아이템 이미지">
                    <div class="item_info">
                        <div class="item_name_area">
                            <p class="item_name">${itemName}</p>
                            <div class="btn-remove">
                                <i class="fa-solid fa-trash fa-2xl" style="color: var(--white-color); padding: 5px;"></i>
                            </div>
                        </div>
                        <p class="item_info_label">남은 시간</p>
                        <div class="item_reset_time_input">
                            <input type="number" class="item_reset_time_days" placeholder="일" min="0" max="30" default="0" value="0" style="width: 50px; margin-left: 10px;">
                            <input type="number" class="item_reset_time_hours" placeholder="시간" min="0" max="23" default="0" value="0" style="width: 50px; margin-left: 5px;">
                            <input type="number" class="item_reset_time_minutes" placeholder="분" min="0" max="59" default="0" value="0" style="width: 50px; margin-left: 5px;">
                            <input type="number" class="item_reset_time_seconds" placeholder="초" min="0" max="59" default="0" value="0" style="width: 50px; margin-left: 5px;">
                        </div>
                    </div>
                </div>
            </div>
        `);
    $('#user_items').append(itemElement);
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

//  Input에 아이템 ResetTime 일, 시간, 분, 초 값 넣어주기
function setItemResetTimeInputs(itemElement, item) {
    const itemResetTime = new Date(item.resetTime);
    const now = new Date();
    const diff = itemResetTime - now;

    let days, hours, minutes, seconds;

    if (diff > 0) {
        days = Math.floor(diff / (1000 * 60 * 60 * 24));
        hours = Math.floor((diff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        seconds = Math.floor((diff % (1000 * 60)) / 1000);
    } else {
        days = hours = minutes = seconds = 0;
    }

    itemElement.find('.item_reset_time_days').val(days);
    itemElement.find('.item_reset_time_hours').val(hours);
    itemElement.find('.item_reset_time_minutes').val(minutes);
    itemElement.find('.item_reset_time_seconds').val(seconds);

    itemElement.find('.item_reset_time_days').data('item-reset-time-days', days);
    itemElement.find('.item_reset_time_hours').data('item-reset-time-hours', hours);
    itemElement.find('.item_reset_time_minutes').data('item-reset-time-minutes', minutes);
    itemElement.find('.item_reset_time_seconds').data('item-reset-time-seconds', seconds);
}

// 내 가방 설정 가져오기
function fetchUserItemSetting() {
    const itemListContainer = $('#user_items');

    $.get(`/api/v1/users/items`, function(data) {
        itemListContainer.empty(); // 기존 아이템 리스트 초기화

        data.forEach(item => {
            const itemElement = $(`
                <div class="col-12 col-md-6 col-lg-4 mb-2 mt-2">
                    <div class="item" data-item-id="${item.itemId}" data-user-item-id="${item.userItemId}">
                        <div class="item_area">
                            <img class="item_image" src="${item.imageUrl}" alt="아이템 이미지">
                            <div class="item_info">
                                <div class="item_name_area">
                                    <p class="item_name">${item.name}</p>
                                    <div class="btn-remove">
                                        <i class="fa-solid fa-trash fa-2xl" style="color: var(--white-color); padding: 5px;"></i>
                                    </div>
                                </div>
                                <p class="item_info_label">남은 시간</p>
                                <div class="item_reset_time_input">
                                    <input type="number" class="item_reset_time_days" placeholder="일" min="0" max="30" default="0" style="width: 50px; margin-left: 10px;">
                                    <input type="number" class="item_reset_time_hours" placeholder="시간" min="0" max="23" default="0" style="width: 50px; margin-left: 5px;">
                                    <input type="number" class="item_reset_time_minutes" placeholder="분" min="0" max="59" default="0" style="width: 50px; margin-left: 5px;">
                                    <input type="number" class="item_reset_time_seconds" placeholder="초" min="0" max="59" default="0" style="width: 50px; margin-left: 5px;">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            `);
            itemListContainer.append(itemElement);
            setItemResetTimeInputs(itemElement, item);
        });
    }).fail(function(error) {
        console.error('Error fetching items:', error);
    });
}

// 아이템 리스트 가져오기
function fetchItems(url) {
    const itemListContainer = $('#items');

    $.get(url, function(data) {
        itemListContainer.empty(); // 기존 아이템 리스트 초기화
        data.forEach(item => {
            const itemElement = $(`
                <div class="col-12 col-md-6 col-lg-4 mb-3 mt-3">
                    <div class="item" data-item-id="${item.id}">
                        <div class="item_head_area">
                            <img class="item_head_image" src="${item.imageUrl}" alt="아이템 이미지">
                            <div class="item_head_info">
                                <div class="item_head_name_area">
                                    <p class="item_head_name">${item.name}</p>
                                </div>
                                <p class="item_head_effect">${item.effect}</p>
                            </div>
                        </div>
                        <div class="btn-add">
                            <i class="fa-solid fa-plus fa-2xl" style="color: var(--white-color); padding: 5px;"></i>
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

// 저장 버튼 클릭 이벤트
$('.btn-save').on('click', function() {
    const itemListContainer = $('#user_items');
    const items = itemListContainer.find('.item');

    const UserItemListSaveRequestDto = [];
    const UserItemListUpdateRequestDto = [];
    const UserItemListDeleteRequestDto = [];

    items.each(function() {
        const item = $(this);
        const itemId = item.data('item-id');
        const userItemId = item.data('user-item-id') !== undefined ? item.data('user-item-id') : null;

        // original 시간 data 가져오기
        const originalResetTimeDays = parseInt(item.find('.item_reset_time_days').data('item-reset-time-days'), 10);
        const originalResetTimeHours = parseInt(item.find('.item_reset_time_hours').data('item-reset-time-hours'), 10);
        const originalResetTimeMinutes = parseInt(item.find('.item_reset_time_minutes').data('item-reset-time-minutes'), 10);
        const originalResetTimeSeconds = parseInt(item.find('.item_reset_time_seconds').data('item-reset-time-seconds'), 10);

        // 현재 시간 data 가져오기
        const currentResetTimeDays = parseInt(item.find('.item_reset_time_days').val(), 10);
        const currentResetTimeHours = parseInt(item.find('.item_reset_time_hours').val(), 10);
        const currentResetTimeMinutes = parseInt(item.find('.item_reset_time_minutes').val(), 10);
        const currentResetTimeSeconds = parseInt(item.find('.item_reset_time_seconds').val(), 10);

        // 시간 변경 체크
        if (currentResetTimeDays !== originalResetTimeDays ||
            currentResetTimeHours !== originalResetTimeHours ||
            currentResetTimeMinutes !== originalResetTimeMinutes ||
            currentResetTimeSeconds !== originalResetTimeSeconds) {

            const currentTime = new Date(Date.now());
            const days = currentResetTimeDays;
            const hours = currentResetTimeHours;
            const minutes = currentResetTimeMinutes;
            const seconds = currentResetTimeSeconds;
            
            console.log(days, hours, minutes, seconds);

            if (days < 0 || days > 30) {
                alert('날짜는 0에서 30일 사이여야 합니다.');
                return;
            }
            if (hours < 0 || hours > 23) {
                alert('시간은 0에서 23시간 사이여야 합니다.');
                return;
            }
            if (minutes < 0 || minutes > 59) {
                alert('분은 0에서 59분 사이여야 합니다.');
                return;
            }
            if (seconds < 0 || seconds > 59) {
                alert('초는 0에서 59초 사이여야 합니다.');
                return;
            }

            const itemResetTime = new Date(currentTime.getTime() + 
                days * 24 * 60 * 60 * 1000 + 
                (hours+9) * 60 * 60 * 1000 + 
                minutes * 60 * 1000 + 
                seconds * 1000
            ).toISOString().split('.')[0];

            console.log(itemResetTime);

            if (userItemId === null) {
                console.log('새로 생성');
                UserItemListSaveRequestDto.push({
                    itemId: itemId,
                    resetTime: itemResetTime
                });
            } else {
                console.log('기존 아이템 수정');
                UserItemListUpdateRequestDto.push({
                    userItemId: userItemId,
                    resetTime: itemResetTime
                });
            }
        }

        // 삭제 체크 (기존 존재하던 아이템들 중 (userItemId를 가지고 있는) 여기에 없는 아이템은 삭제)
        if (userItemId) {
            UserItemListDeleteRequestDto.push({
                userItemId: userItemId
            });
        }
    });

    let deleteSuccess = false;
    let saveSuccess = false;
    let updateSuccess = false;

    // 내 아이템 항목 삭제 API
    $.ajax({
        url: '/api/v1/users/items',
        method: 'DELETE',
        data: JSON.stringify(UserItemListDeleteRequestDto),
        contentType: 'application/json',
        success: function(response) {
            console.log('내 아이템 항목 삭제 성공');
            deleteSuccess = true;
            checkAllSuccess();
        },
        error: function(error) {
            console.error('Error deleting items:', error);
            alert('내 아이템 항목 삭제 실패');
        }
    });

    // 내 아이템 항목 생성 API
    if (UserItemListSaveRequestDto.length > 0) {
        $.ajax({
            url: '/api/v1/users/items',
            method: 'POST',
            data: JSON.stringify(UserItemListSaveRequestDto),
            contentType: 'application/json',
            success: function(response) {
                console.log('내 가방 항목 생성 성공');
                saveSuccess = true;
                checkAllSuccess();
            },
            error: function(error) {
                console.error('Error creating items:', error);
                alert('내 가방 설정 실패');
            }
        });
    } else {
        saveSuccess = true;
        checkAllSuccess();
    }

    // 내 아이템 항목 수정 API
    if (UserItemListUpdateRequestDto.length > 0) {
        $.ajax({
            url: '/api/v1/users/items',
            method: 'PUT',
            data: JSON.stringify(UserItemListUpdateRequestDto),
            contentType: 'application/json',
            success: function(response) {
                console.log('내 가방 항목 수정 성공');
                updateSuccess = true;
                checkAllSuccess();
            },
            error: function(error) {
                console.error('Error updating items:', error);
                alert('내 가방 설정 실패');
            }
        });
    } else {
        updateSuccess = true;
        checkAllSuccess();
    }

    function checkAllSuccess() {
        if (deleteSuccess && saveSuccess && updateSuccess) {
            alert('내 가방 설정 완료');
            location.href = '/user/item';
        }
    }

});


