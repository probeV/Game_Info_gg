// 필터 기능 및 숙제 체크 기능

document.addEventListener('DOMContentLoaded', () => {
    updateTasks(); // 페이지 로드 시 기본 필터링 적용
});

document.querySelectorAll('input[name="mode"]').forEach(input => {
    input.addEventListener('change', updateTasks); // 모드 변경 시 작업 목록 업데이트
});

document.querySelectorAll('input[name="event"]').forEach(input => {
    input.addEventListener('change', updateTasks); // 이벤트 변경 시 작업 목록 업데이트
});

// 작업 목록을 서버로부터 가져오는 함수
function updateTasks() {
    const mode = document.querySelector('input[name="mode"]:checked').value.toUpperCase(); // 선택된 모드 값
    const event = document.querySelector('input[name="event"]:checked').value.toUpperCase(); // 선택된 이벤트 값

    // 서버에 필터에 맞는 작업 목록을 GET 요청으로 가져오기
    fetch(`/api/v1/tasks/filter?mode=${mode}&event=${event}`, {
        method: 'GET'
    })
        .then(response => response.json())  // 응답을 JSON으로 파싱
        .then(tasks => {
            const tasksContainer = document.querySelector('.tasks-container');
            tasksContainer.innerHTML = ''; // 이전 내용 제거

            const pveTasks = tasks.filter(task => task.modeType === 'PVE');
            const pvpTasks = tasks.filter(task => task.modeType === 'PVP');

            if(mode == 'PVE'){
                renderTasks('PVE', pveTasks, tasksContainer, event);
            }
            else if(mode == 'PVP'){
                renderTasks('PVP', pvpTasks, tasksContainer, event);
            }
            else{
                renderTasks('PVE', pveTasks, tasksContainer, event);
                renderTasks('PVP', pvpTasks, tasksContainer, event);
            }
        })
        .catch(error => console.error('Error fetching tasks:', error));  // 에러 처리
}

function renderTasks(modeType, tasks, container, event) {
    const weeklyTasks = tasks.filter(task => task.frequencyType === 'WEEKLY');
    const dailyTasks = tasks.filter(task => task.frequencyType === 'DAILY');

    const modeContainer = document.createElement('div');
    modeContainer.className = 'task-category';
    modeContainer.innerHTML = `<h2>[${modeType}]</h2>`;

    if (weeklyTasks.length > 0 || dailyTasks.length > 0) {
        const taskList = document.createElement('div');
        taskList.className = 'task-list';

        // 주간 작업 컨테이너 생성
        const weeklyContainer = document.createElement('div');
        weeklyContainer.innerHTML = '<h3><주간(WEEK)></h3>';

        // 주간 작업이 없을 경우 메시지 추가
        if(weeklyTasks.length == 0){
            const message = getEventMessage(event);
            weeklyContainer.innerHTML += `<p>${message}</p>`;
        }

        // 주간 작업 목록 추가
        weeklyTasks.forEach(task => {
            const taskElement = document.createElement('div');
            taskElement.className = 'task';
            taskElement.textContent = task.name;
            taskElement.onclick = () => toggleTask(taskElement);
            weeklyContainer.appendChild(taskElement);
        });

        // 일일 작업 컨테이너 생성
        const dailyContainer = document.createElement('div');
        dailyContainer.innerHTML = '<h3><일일(DAY)></h3>';

        // 일일 작업이 없을 경우 메시지 추가
        if(dailyTasks.length == 0){
            const message = getEventMessage(event);

            dailyContainer.innerHTML += `<p>${message}</p>`;
        }

        // 일일 작업 목록 추가
        dailyTasks.forEach(task => {
            const taskElement = document.createElement('div');
            taskElement.className = 'task';
            taskElement.textContent = task.name;
            taskElement.onclick = () => toggleTask(taskElement);
            dailyContainer.appendChild(taskElement);
        });

        taskList.appendChild(weeklyContainer);
        taskList.appendChild(dailyContainer);
        modeContainer.appendChild(taskList);
    } else {
        // 해당 모드의 작업이 없을 경우 메시지 추가
        const taskList = document.createElement('div');
        taskList.className = 'task-list';

        const message = getEventMessage(event);

        const weeklyContainer = document.createElement('div');
        weeklyContainer.innerHTML = '<h3><주간(WEEK)></h3>'+`<p>${message}</p>`;

        const dailyContainer = document.createElement('div');
        dailyContainer.innerHTML = '<h3><일일(DAY)></h3>'+`<p>${message}</p>`;

        taskList.appendChild(weeklyContainer);
        taskList.appendChild(dailyContainer);
        modeContainer.appendChild(taskList);
    }

    container.appendChild(modeContainer);
}

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

function toggleTask(taskElement) {
    taskElement.classList.toggle('completed');
}