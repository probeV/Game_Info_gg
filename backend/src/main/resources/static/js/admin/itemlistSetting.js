$(document).ready(function() {
    // 초기 아이템 리스트 로드
    fetchItems();

    // 삭제 버튼 클릭 이벤트
    $(document).on('click', '.item_delete_button', function() {
        const itemId = $(this).closest('.item').data('item-id');
        const preImageUrl = $(this).closest('.item').find('.item_head_area').data('pre-image-url');

        // 저장 되어 있던 아이템
        if (itemId) {
            if (confirm('정말로 삭제하겠습니까?')) { // 삭제 확인

                // S3 파일 삭제 API / imageUrl 이 존재할 때
                if(preImageUrl !== "") {
                    const FileDeleteRequestDto = {
                        imageUrl: preImageUrl
                    };

                    $.ajax({
                        url: `/api/v1/admins/files`,
                        type: 'DELETE',
                        contentType: 'application/json',
                        data: JSON.stringify(FileDeleteRequestDto),
                        success: function (response) {

                        },
                        error: function (error) {
                            console.error('Error deleting item:', error);
                            alert('파일 삭제 중 오류가 발생했습니다.')
                        }
                    })
                }

                // 아이템 항목 삭제 API
                $.ajax({
                    url: `/api/v1/admins/items/${itemId}`,
                    type: 'DELETE',
                    success: function(response) {
                        alert('아이템이 성공적으로 삭제되었습니다.');
                        fetchItems();
                    },
                    error: function(error) {
                        console.error('Error deleting item:', error);
                        alert('아이템 삭제 중 오류가 발생했습니다.');
                    }
                });

                $(this).closest('.item').remove();
                fetchItems();
            }
        }
        // 저장 안된 아이템은 그냥 바로 삭제
        else{
            $(this).closest('.item').remove();

            fetchItems();
        }
    });

    // 하이라이트 버튼 클릭 이벤트
    $(document).on('click', '.item_highlight_button', function() {
        const itemId = $(this).closest('.item').data('item-id');

        console.log(itemId);
        // const selectedText = window.getSelection();
        // if (selectedText.rangeCount) {
        //     const range = selectedText.getRangeAt(0);
        //     const span = document.createElement('span');
        //     span.style.backgroundColor = 'var(--main-color)';
        //     range.surroundContents(span);
        // }
    });

    // 파일 선택 이벤트 핸들러
    $(document).on('change', '.item input[type="file"]', function(e) {
        const reader = new FileReader();
        const imgElement = $(this).closest('.item').find('.image');
        reader.onload = function(e) {
            imgElement.attr('src', e.target.result);
        };
        reader.readAsDataURL(this.files[0]);
    });

    // 저장 버튼 클릭 이벤트
    $(document).on('click', '.item_save_button', function() {
        const itemId = $(this).closest('.item').data('item-id');
        const itemName = $(this).closest('.item').find('.item_head_name').val();
        const itemEffect = $(this).closest('.item').find('.item_head_effect').val();
        const itemDescription = $(this).closest('.item').find('.item_body_info_description').val();
        const preImageUrl = $(this).closest('.item').find('.item_head_area').data('pre-image-url');
        const selectedImageFile = $(this).closest('.item').find('.item_image_file').prop('files')[0];


        console.log(itemId);
        console.log(itemName);
        console.log(itemEffect);
        console.log(itemDescription);
        console.log(preImageUrl);
        console.log(selectedImageFile);

        let imageUrl;

        if(selectedImageFile!=null && preImageUrl !== ""){
            // 새로 들어온 File 이 존재할 때 + 이전에 저장되어있던 File (preImageUrl) 이 존재 할 때
            // 파일 수정 로직
            imageUrl = updateFile(preImageUrl, "items", selectedImageFile);
        }
        else if(selectedImageFile!=null && preImageUrl === ""){
            // 새로 들어온 File 이 존재할 때 + 이전에 저장되어있던 File 이 없을 때
            // 파일 생성 로직   
            imageUrl = createFile("items", selectedImageFile);
        }
        else{
            // 새로 들어온 File 이 없을 때
            // 파일 로직 x, preImageUrl 이용 
            imageUrl = preImageUrl;
        }

        console.log(imageUrl);

        imageUrl.then(function(response){
            console.log(response);

            // itemId가 있다면 수정 로직
            if (itemId) {
                const ItemUpdateRequestDto={
                    name: itemName,
                    effect: itemEffect,
                    description: itemDescription,
                    imageUrl: imageUrl
                }

            // 아이템 항목 수정 API
            $.ajax({
                url: `/api/v1/admins/items/${itemId}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(ItemUpdateRequestDto),
                success: function (response){
                    alert('아이템이 성공적으로 수정되었습니다.');
                    fetchItems();
                },
                error: function (error){
                    console.error('Error updating file:', error);
                    alert('아이템 수정 중 오류가 발생했습니다.')
                }
                })
            }
            // itemId가 없다면 아이템 생성 로직
            else {
                const ItemSaveRequestDto = {
                    name: itemName,
                    effect: itemEffect,
                    description: itemDescription,
                    imageUrl: imageUrl
                }

                // 아이템 항목 생성 API
                $.ajax({
                    url: '/api/v1/admins/items',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(ItemSaveRequestDto),
                    success: function(response) {
                        alert('아이템이 성공적으로 생성되었습니다.');
                        fetchItems();
                    },
                    error: function(error) {
                        console.error('Error creating item:', error);
                        alert('아이템 생성 중 오류가 발생했습니다.');
                    }
                });
            }
        })
    });
});

// S3 File 수정 API
function updateFile(preImageUrl, directoryPath, selectedImageFile){
    const formData = new FormData();

    const FileUpdateRequestDto = {
        preFileUrl: preImageUrl,
        directoryPath: directoryPath
    };

    formData.append('file', selectedImageFile);
    formData.append('FileUpdateRequestDto', new Blob([JSON.stringify(FileUpdateRequestDto)], { type: "application/json" }));

    // S3 파일 수정
    return $.ajax({
        url: `/api/v1/admins/files`,
        type: 'PUT',
        data: formData,
        processData: false,
        contentType: false
    }).fail(function(error) {
        console.error('Error updating file:', error);
        alert('파일 수정 중 오류가 발생했습니다.');
    });
}

// S3 File 생성 API
function createFile(directoryPath, selectedImageFile){
    const formData = new FormData();

    const FileSaveRequestDto = {
        directoryPath: directoryPath
    };

    formData.append('file', selectedImageFile);
    formData.append('FileSaveRequestDto', new Blob([JSON.stringify(FileSaveRequestDto)], { type: "application/json" }));

    // S3 파일 생성
    return $.ajax({
        url: `/api/v1/admins/files`,
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false
    }).fail(function(error) {
        console.error('Error creating file:', error);
        alert('파일 생성 중 오류가 발생했습니다.');
    });
}

// 검색 버튼 클릭 이벤트
$('.search i').on('click', function() {
    fetchItems();
});

// 엔터 키 이벤트
$('.search input').on('keydown', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // 기본 엔터키 동작 방지
        fetchItems();
    }
});

// 새 아이템 추가 버튼 클릭 이벤트
$(document).on('click', '.new_item_button', function() {
    const itemElement = $(`
        <div class="col-12 col-md-6 col-lg-4 mb-3 mt-3">
            <div class="item" data-item-id="" >

                <div class="item_head_area" data-pre-image-url="">
                    <img class="image" src="" alt="아이템 이미지">
                    <div class="item_head_info">
                        <div class="item_head_name_area">
                            <input type="text" class="item_head_name" value="">
                        </div>
                        <textarea class="item_head_effect"></textarea>
                    </div>
                </div>

                <div class="item_body_area">
                    <div class="item_body_info">
                        <textarea class="item_body_info_description"></textarea>
                    </div>
                </div>

                <div class="item_button_area">
                    <button class="item_highlight_button">하이라이트</button>
                    <button class="item_delete_button">삭제</button>
                    <button class="item_save_button">저장</button>
                </div>

                <input type="file" class="item_image_file" accept="image/png, image/jpeg">

            </div>
        </div>
    `);
    $('#items').append(itemElement);
});

function fetchItems() {

    let url=null;
    const keyword = $('.search input').val().trim() || null;
    if (keyword) {
        url=`/api/v1/items/search?keyword=${keyword}`;
    }
    else {
        url='/api/v1/items';
    }

    const itemListContainer = $('#items');

    $.get(url, function(data) {
        itemListContainer.empty(); // 기존 아이템 리스트 초기화
        data.forEach(item => {
            const itemElement = $(`
                <div class="col-12 col-md-6 col-lg-4 mb-3 mt-3">
                    <div class="item" data-item-id="${item.id}" >

                        <div class="item_head_area" data-pre-image-url="${item.imageUrl}">
                            <img class="image" src="${item.imageUrl}" alt="아이템 이미지">
                            <div class="item_head_info">
                                <div class="item_head_name_area">
                                    <input type="text" class="item_head_name" value="${item.name}">
                                </div>
                                <textarea class="item_head_effect">${item.effect}</textarea>
                            </div>
                        </div>

                        <div class="item_body_area">
                            <div class="item_body_info">
                                <textarea class="item_body_info_description">${item.description}</textarea>
                            </div>
                        </div>

                        <div class="item_button_area">
                            <button class="item_highlight_button">하이라이트</button>
                            <button class="item_delete_button">삭제</button>
                            <button class="item_save_button">저장</button>
                        </div>

                        <input type="file" class="item_image_file" accept="image/png, image/jpeg">

                    </div>
                </div>
            `);
            itemListContainer.append(itemElement);
        });
    }).fail(function(error) {
        console.error('Error fetching items:', error);
    });
}