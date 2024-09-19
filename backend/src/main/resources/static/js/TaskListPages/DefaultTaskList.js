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
    $.get(`/api/v1/tasks/filter?mode=${mode}&event=${event}`, function(tasks) {
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

function renderTasks(modeType, tasks, container, eventType) {
    const weeklyTasks = tasks.filter(task => task.frequencyType === 'WEEKLY');
    const dailyTasks = tasks.filter(task => task.frequencyType === 'DAILY');

    const modeContainer = $('<div>', { class: 'task-category' }).html(`<h2>${modeType}</h2>`);

    if (weeklyTasks.length > 0 || dailyTasks.length > 0) {
        const taskList = $('<div>', { class: 'task-list' });

        // 주간 컨테이너 생성
        const weeklyContainer = $('<div>').html(`<h3>주간(WEEKLY)</h3> <button class="reset-button btn btn-warning btn-sm" modeType="${modeType}" frequencyType="WEEKLY">${modeType} 주간 초기화</button>`);

        if(weeklyTasks.length == 0){
            const message = getEventMessage(eventType);
            weeklyContainer.html(`<h3>주간(WEEKLY)</h3><p>${message}</p>`);
        }

        weeklyTasks.forEach(task => {
            const taskElement = createTaskElement(task);

            if(modeType == 'PVE'){
                let pveWeeklyTasks = JSON.parse(localStorage.getItem('pveWeeklyTasks')) || [];
                if(!pveWeeklyTasks.includes(task.id)){
                    pveWeeklyTasks.push(task.id);
                    localStorage.setItem('pveWeeklyTasks', JSON.stringify(pveWeeklyTasks));
                }
            }
            if(modeType == 'PVP'){
                let pvpWeeklyTasks = JSON.parse(localStorage.getItem('pvpWeeklyTasks')) || [];
                if(!pvpWeeklyTasks.includes(task.id)){
                    pvpWeeklyTasks.push(task.id);
                    localStorage.setItem('pvpWeeklyTasks', JSON.stringify(pvpWeeklyTasks));
                }
            }

            weeklyContainer.append(taskElement);
        });

        // 일일 컨테이너 생성
        const dailyContainer = $('<div>').html(`<h3>일간(DAILY)</h3> <button class="reset-button btn btn-warning btn-sm" modeType="${modeType}" frequencyType="DAILY">${modeType} 일일 초기화</button>`);

        if(dailyTasks.length == 0){
            const message = getEventMessage(eventType);
            dailyContainer.html(`<h3>일간(DAILY)</h3><p>${message}</p>`);
        }

        dailyTasks.forEach(task => {
            const taskElement = createTaskElement(task);

            if(modeType == 'PVE'){
                let pveDailyTasks = JSON.parse(localStorage.getItem('pveDailyTasks')) || [];
                if(!pveDailyTasks.includes(task.id)){
                    pveDailyTasks.push(task.id);
                    localStorage.setItem('pveDailyTasks', JSON.stringify(pveDailyTasks));
                }
            }
            if(modeType == 'PVP'){
                let pvpDailyTasks = JSON.parse(localStorage.getItem('pvpDailyTasks')) || [];
                if(!pvpDailyTasks.includes(task.id)){
                    pvpDailyTasks.push(task.id);
                    localStorage.setItem('pvpDailyTasks', JSON.stringify(pvpDailyTasks));
                }
            }

            dailyContainer.append(taskElement);
        });

        taskList.append(weeklyContainer);
        taskList.append(dailyContainer);
        modeContainer.append(taskList);
    } 
    else {
        const message = getEventMessage(eventType);

        const taskList = $('<div>', {class: 'task-list'});

        const weeklyContainer = $('<div>').html(`<h3>주간(WEEKLY)</h3><p>${message}</p>`);
        taskList.append(weeklyContainer);

        const dailyContainer = $('<div>').html(`<h3>일간(DAILY)</h3><p>${message}</p>`);
        taskList.append(dailyContainer);

        modeContainer.append(taskList);
    }

    container.append(modeContainer);
}

// 숙제 항목 생성 함수
function createTaskElement(task) {
    const taskElement = $('<div>', {class: 'task', 'data-id': task.id}).text(task.name);

    taskElement.on('click', function(){
        toggleTask($(this), task.id);
    });
    
    // 로컬 스토리지에서 완료 상태 복원
    const completedTasks = JSON.parse(localStorage.getItem('completedTasks')) || [];
    if (completedTasks.includes(task.id)) {
        taskElement.addClass('completed'); // jQuery의 addClass 메서드 사용
    }

    return taskElement;
}

// 사용자가 해당 task를 눌렀을 때, 완료 상태로 변경하는 함수
function toggleTask(taskElement, taskId) {
    taskElement.toggleClass('completed');

    // 로컬 스토리지에 완료 상태 가져오기
    let completedTasks = JSON.parse(localStorage.getItem('completedTasks')) || [];

    // 완료 상태 변경
    if (taskElement.hasClass('completed')) {
        completedTasks.push(taskId);
    } else {
        completedTasks = completedTasks.filter(id => id !== taskId);
    }
    localStorage.setItem('completedTasks', JSON.stringify(completedTasks));
}

// 이벤트 없을 때 메시지 생성 함수
function getEventMessage(event) {
    switch (event) {
        case 'ALL':
            return '이벤트가 없습니다';
        case 'NORMAL':
            return '일반 이벤트가 없습니다';
        case 'TIME':
            return '시간 이벤트가 없습니다';
        case 'SPECIAL':
            return '특별 이벤트가 없습니다';
        default:
            return '이벤트가 없습니다';
    }
}