import { renderTasks } from './taskListSettingRender.js';

let length = 0;

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
    $.get(`/api/v1/tasks/filters?mode=ALL&event=ALL`, function(tasks) {
        const tasksContainer = $('.tasks-area');
        tasksContainer.empty(); // 이전 내용 제거

        length = tasks.length;

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
    const tasksToDelete = [];

    $('.task-list-weekly .task, .task-list-daily .task').each(function() {
        const taskId = $(this).data('id') || null;
        const name = $(this).find('input[type="text"]').val();
        const eventType = $(this).find('select').val();

        const mode = $(this).closest('[data-mode-type]').data('mode-type');
        const frequency = $(this).closest('[data-frequency-type]').data('frequency-type');

        // 초기 상태와 비교
        const preTaskName = $(this).data('name');
        const preEventType = $(this).data('event-type');

        console.log(preTaskName, name, preEventType, eventType);
        console.log(preTaskName !== name, preEventType !== eventType);

        // 수정
        if (preTaskName !== name || preEventType !== eventType) {
            tasksToSave.push({ id: taskId, name, mode, frequency, event: eventType });
        }
        // 생성
        else if(taskId == null) {
            tasksToSave.push({ id: taskId, name, mode, frequency, event: eventType });
        }

        // 삭제
        if(taskId != null) {
            tasksToDelete.push({ id: taskId });
        }
    });

    console.log(tasksToSave, tasksToDelete);

    if (tasksToSave.length > 0) {
        // 서버에 저장 요청
        $.ajax({
            url: '/api/v1/admins/tasks',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(tasksToSave),
            success: function(response) {
                console.log('저장되었습니다.', response);
            },
            error: function(error) {
                alert('작업 저장 중 오류 발생:', error);
                return;
            }
        });
    }

    if(tasksToDelete.length != length) {
        // 서버에 삭제 요청
        $.ajax({
            url: '/api/v1/admins/tasks',
            type: 'DELETE',
            contentType: 'application/json',
            data: JSON.stringify(tasksToDelete),
            success: function(response) {
                console.log('삭제되었습니다.', response);
        },
            error: function(error) {
                alert('작업 삭제 중 오류 발생:', error);
                return;
            }
        });
    }   

    alert('작업이 성공적으로 저장되었습니다.');
    updateTasks();
}


