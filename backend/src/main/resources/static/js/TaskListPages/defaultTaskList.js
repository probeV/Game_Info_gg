import { renderTasks } from './taskListRender.js';
import { getEventMessage } from './eventUtils.js';

$(document).ready(function() {
    updateTasks(); // 페이지 로드 시 기본 필터링 적용

    // 초기화 버튼 클릭 이벤트 핸들러 (이벤트 위임 사용)
    $('body').on('click', '.reset-button', function(event) {
        const modeType = $(this).attr('modeType');
        const frequencyType = $(this).attr('frequencyType');

        let completedTasks = JSON.parse(localStorage.getItem('completedTasks')) || [];
        let resetTasks = JSON.parse(localStorage.getItem(`${modeType.toLowerCase()}${frequencyType.charAt(0).toUpperCase() + frequencyType.slice(1).toLowerCase()}Tasks`)) || [];

        // 완료 상태인 task를 "미완료" 상태로 변경
        resetTasks.forEach(function(taskId) {
            const task = $(`.task[data-id="${taskId}"]`);
            console.log(task);
            if (task.length) {
                task.removeClass('completed'); // "완료" 상태 클래스 제거
                completedTasks = completedTasks.filter(id => id !== taskId);
            }
        });

        // 로컬 스토리지 업데이트
        localStorage.setItem('completedTasks', JSON.stringify(completedTasks));
    });
});

// 필터 버튼 변경 감지 (mode)
$('input[name="mode"]').change(function() {
    updateTasks();
});

// 필터 버튼 변경 감지 (event)
$('input[name="event"]').change(function() {
    updateTasks();
});

// 작업 목록을 서버로부터 가져오는 함수
function updateTasks() {
    const mode = document.querySelector('input[name="mode"]:checked').value.toUpperCase(); // 선택된 모드 값
    const event = document.querySelector('input[name="event"]:checked').value.toUpperCase(); // 선택된 이벤트 값

    // 로컬 스토리지에서 주간/일간 작업 목록들 가져오기 
    let pveWeeklyTasks = JSON.parse(localStorage.getItem('pveWeeklyTasks')) || [];
    let pvpWeeklyTasks = JSON.parse(localStorage.getItem('pvpWeeklyTasks')) || [];
    let pveDailyTasks = JSON.parse(localStorage.getItem('pveDailyTasks')) || [];
    let pvpDailyTasks = JSON.parse(localStorage.getItem('pvpDailyTasks')) || [];    
    // 로컬 스토리지 초기화
    localStorage.setItem('pveWeeklyTasks', JSON.stringify([]));
    localStorage.setItem('pvpWeeklyTasks', JSON.stringify([]));
    localStorage.setItem('pveDailyTasks', JSON.stringify([]));
    localStorage.setItem('pvpDailyTasks', JSON.stringify([]));

    // 서버에 필터에 맞는 작업 목록을 GET 요청으로 가져오기
    $.get(`/api/v1/tasks/filters?mode=${mode}&event=${event}`, function(tasks) {
        const tasksContainer = $('.tasks-area');
        tasksContainer.empty(); // 이전 내용 제거

        const pveTasks = tasks.filter(task => task.modeType === 'PVE');
        const pvpTasks = tasks.filter(task => task.modeType === 'PVP');

        if (mode === 'PVE' || mode === 'ALL') {
            renderTasks('PVE', pveTasks, tasksContainer, event);
        }
        if (mode === 'PVP' || mode === 'ALL') {
            renderTasks('PVP', pvpTasks, tasksContainer, event);
        }
    }).fail(function(error) {
        console.error('Error fetching tasks:', error);
    });
}