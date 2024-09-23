import { createTaskElement } from './taskListUtils.js';
import { getEventMessage } from './eventUtils.js';

export function renderTasks(modeType, tasks, container, eventType) {
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