$(document).ready(function() {
    let debounceTimer;

    // jwt_intercepter 설정 완료 이벤트를 감지하여 초기 로드
    $(document).on('jwtIntercepterReady', function() {
        loadUserList(0);
    });

    $(document).on('click', '.page-link', function(e) {
        e.preventDefault();
        const page = $(this).data('page');

        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(function() {
            loadUserList(page);
        }, 300); // 300ms 디바운스 타임
    });
});

function loadUserList(page) {

    $.get(`/api/v1/admins/users?page=${page}`, function(userPage) {
        const tbody = $('tbody');
        tbody.empty(); // 기존 내용 삭제

        if (userPage.userList.length > 0) {
            userPage.userList.forEach(function(user) {
                tbody.append(`
                    <tr>
                        <td style="width: 10%;">${user.id}</td>
                        <td style="width: 30%;">${user.email}</td>
                        <td style="width: 10%;">${user.name}</td>
                        <td style="width: 10%;">${user.roleType}</td>
                        <td style="width: 10%;">${user.provider}</td>
                        <td style="width: 30%;">${user.createdDate}</td>
                    </tr>
                `);
            });
        } else {
            tbody.append(`
                <tr>
                    <td colspan="6" style="text-align: center;">유저가 없습니다.</td>
                </tr>
            `);
        }

        // 페이지네이션 업데이트
        const pagination = $('.pagination');
        pagination.empty();

        const totalPages = userPage.totalPages;
        const currentPage = userPage.number;

        // 처음으로 가는 버튼
        pagination.append(`
            <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="0"><<</a>
            </li>
        `);

        // 페이지네이션 범위 계산
        let startPage = Math.max(0, currentPage - 2);
        let endPage = Math.min(totalPages - 1, currentPage + 2);

        // Adjust startPage and endPage if they are out of bounds
        if (currentPage <= 2) {
            endPage = Math.min(4, totalPages - 1);
        } else if (currentPage >= totalPages - 3) {
            startPage = Math.max(totalPages - 5, 0);
        }

        for (let i = startPage; i <= endPage; i++) {
            pagination.append(`
                <li class="page-item ${i === currentPage ? 'active' : ''}">
                    <a class="page-link" href="#" data-page="${i}">${i + 1}</a>
                </li>
            `);
        }

        // 마지막으로 가는 버튼
        pagination.append(`
            <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
                <a class="page-link" href="#" data-page="${totalPages - 1}">>></a>
            </li>
        `);
    });
}