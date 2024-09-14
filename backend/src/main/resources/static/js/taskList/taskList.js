// 필터 기능 및 숙제 체크 기능

document.addEventListener('DOMContentLoaded', () => {
    updateTasks(); // 페이지 로드 시 기본 필터링 적용

    // 초기화 버튼 클릭 이벤트 핸들러 (이벤트 위임 사용)
    document.body.addEventListener('click', function(event) {
        if (event.target.classList.contains('reset-button')) {
            const modeType = event.target.getAttribute('modeType');
            const frequencyType = event.target.getAttribute('frequencyType');

            let completedTasks = JSON.parse(localStorage.getItem('completedTasks')) || [];
            
            let resetTasks = JSON.parse(localStorage.getItem(`${modeType.toLowerCase()}${frequencyType.charAt(0).toUpperCase() + frequencyType.slice(1).toLowerCase()}Tasks`)) || [];
            
            // 완료 상태인 task를 "미완료" 상태로 변경
            resetTasks.forEach(taskId => {
                const task = document.querySelector(`.task[data-id="${taskId}"]`);
                console.log(task);
                if (task) {
                    task.classList.remove('completed'); // "완료" 상태 클래스 제거
                    console.log(taskId);
                    completedTasks = completedTasks.filter(id => id !== taskId);
                }
            });

            // 로컬 스토리지 업데이트
            localStorage.setItem('completedTasks', JSON.stringify(completedTasks));

            // // UI 업데이트: 완료 상태를 "미완료"로 변경
            // document.querySelectorAll('.task.completed').forEach(task => {
            //     task.classList.remove('completed');
            // });
        }
    });
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
    fetch(`/api/v1/tasks/filter?mode=${mode}&event=${event}`, {
        method: 'GET'
    })
        .then(response => response.json())  // 응답을 JSON으로 파싱
        .then(tasks => {
            const tasksContainer = document.querySelector('.tasks-container');
            tasksContainer.innerHTML = ''; // 이전 내용 제거

            const pveTasks = tasks.filter(task => task.modeType === 'PVE');
            const pvpTasks = tasks.filter(task => task.modeType === 'PVP');

            if (mode === 'PVE' || mode === 'ALL') {
                renderTasks('PVE', pveTasks, tasksContainer, event);
            }
            if (mode === 'PVP' || mode === 'ALL') {
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
        weeklyContainer.innerHTML = `<h3><주간(WEELK)></h3> <button class="reset-button" modeType="${modeType}" frequencyType="WEEKLY">${modeType} 주간 초기화</button>`;

        if(weeklyTasks.length == 0){
            const message = getEventMessage(event);
            weeklyContainer.innerHTML = `<h3><주간(WEELK)></h3>`+`<p>${message}</p>`;
        }
        // 주간 작업 목록 추가
        weeklyTasks.forEach(task => {
            const taskElement = createTaskElement(task);

            // 주간 작업 로컬 스토리지에 저장
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

            weeklyContainer.appendChild(taskElement);
        });

        if(dailyTasks.length == 0){
            const message = getEventMessage(event);
            dailyContainer.innerHTML = `<h3><일일(DAILY)></h3>`+`<p>${message}</p>`;
        }
        // 일일 작업 컨테이너 생성
        const dailyContainer = document.createElement('div');
        dailyContainer.innerHTML = `<h3><일일(DAILY)></h3> <button class="reset-button" modeType="${modeType}" frequencyType="DAILY">${modeType} 일일 초기화</button>`;

        // 일일 작업 목록 추가
        dailyTasks.forEach(task => {
            const taskElement = createTaskElement(task);

            // 일일 작업 로컬 스토리지에 저장
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

            dailyContainer.appendChild(taskElement);
        });

        taskList.appendChild(weeklyContainer);
        taskList.appendChild(dailyContainer);
        modeContainer.appendChild(taskList);
    } else {
        const message = getEventMessage(event);

        const taskList = document.createElement('div');
        taskList.className = 'task-list';

        const weeklyContainer = document.createElement('div');
        weeklyContainer.innerHTML = `<h3><주간(WEELK)></h3><p>${message}</p>`;
        taskList.appendChild(weeklyContainer); // 주간 컨테이너를 taskList에 추가

        const dailyContainer = document.createElement('div');
        dailyContainer.innerHTML = `<h3><일일(DAILY)></h3><p>${message}</p>`;
        taskList.appendChild(dailyContainer); // 일일 컨테이너를 taskList에 추가

        modeContainer.appendChild(taskList); // modeContainer에 taskList 추가
    }

    container.appendChild(modeContainer);
}

// 숙제 항목 생성 함수
function createTaskElement(task) {
    const taskElement = document.createElement('div');
    taskElement.className = 'task';
    taskElement.textContent = task.name;
    taskElement.setAttribute('data-id', task.id)
    taskElement.onclick = () => toggleTask(taskElement, task.id);
    
    // 로컬 스토리지에서 완료 상태 복원
    const completedTasks = JSON.parse(localStorage.getItem('completedTasks')) || [];
    if (completedTasks.includes(task.id)) {
        taskElement.classList.add('completed');
    }

    return taskElement;
}

// 사용자가 해당 task를 눌렀을 때, 완료 상태로 변경하는 함수
function toggleTask(taskElement, taskId) {
    taskElement.classList.toggle('completed');

    // 로컬 스토리지에 완료 상태 가져오기
    let completedTasks = JSON.parse(localStorage.getItem('completedTasks')) || [];

    // 완료 상태 변경
    if (taskElement.classList.contains('completed')) {
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