import { renderTasks } from './taskListSettingRender.js';

$(document).ready(function() {
    updateTasks(); // 페이지 로드 시 기본 필터링 적용

    // 저장 버튼 클릭 이벤트 추가
    $('.btn').click(function() {
        saveTasks();
    });
});

// 작업 목록을 서버로부터 가져오는 함수
function updateTasks() {
    // 서버에 필터에 맞는 작업 목록을 GET 요청으로 가져오기
    $.get(`/api/v1/tasks/filter?mode=ALL&event=ALL`, function(tasks) {
        const tasksContainer = $('.tasks-area');
        tasksContainer.empty(); // 이전 내용 제거

        const pveTasks = tasks.filter(task => task.modeType === 'PVE');
        const pvpTasks = tasks.filter(task => task.modeType === 'PVP');

        renderTasks('PVE', pveTasks, tasksContainer);
        renderTasks('PVP', pvpTasks, tasksContainer);

    }).fail(function(error) {
        console.error('Error fetching tasks:', error);
    });
}

// 작업을 저장하는 함수
function saveTasks() {
    const tasksToSave = [];

    $('.task').each(function() {
        const taskId = $(this).data('id');
        const name = $(this).find('input[type="text"]').val();
        const eventType = $(this).find('select').val();
        const mode = $(this).hasClass('pve') ? 'PVE' : 'PVP'; // 클래스에 따라 모드 결정
        const frequency = $(this).find('.frequency').val(); // 주기 선택 필드 추가 필요

        tasksToSave.push({ id: taskId, name, mode, frequency, event: eventType });
    });

    // 서버에 저장 요청
    $.ajax({
        url: '/api/v1/tasks/save',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(tasksToSave),
        success: function(response) {
            console.log('작업이 성공적으로 저장되었습니다.', response);
            updateTasks(); // 저장 후 작업 목록 업데이트
        },
        error: function(error) {
            console.error('작업 저장 중 오류 발생:', error);
        }
    });
}


