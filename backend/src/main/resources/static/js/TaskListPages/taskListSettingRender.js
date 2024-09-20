import { createTaskElement } from './taskListSettingUtils.js';

export function renderTasks(modeType, tasks, container) {
    const weeklyTasks = tasks.filter(task => task.frequencyType === 'WEEKLY');
    const dailyTasks = tasks.filter(task => task.frequencyType === 'DAILY');

    const modeContainer = $('<div>', { class: 'task-category' }).html(`<h2>${modeType}</h2>`);

    const taskList = $('<div>', { class: 'task-list' });
    // 주간 컨테이너 생성
    const weeklyContainer = $('<div>', {class: 'task-list-weekly'}).html(`<h3>주간(WEEKLY)</h3>`);
    // 일일 컨테이너 생성
    const dailyContainer = $('<div>', {class: 'task-list-daily'}).html(`<h3>일간(DAILY)</h3>`);

    if (weeklyTasks.length > 0 || dailyTasks.length > 0) {

        weeklyTasks.forEach(task => {
            const taskElement = createTaskElement(task);
            weeklyContainer.append(taskElement);
        });

        dailyTasks.forEach(task => {
            const taskElement = createTaskElement(task);
            dailyContainer.append(taskElement);
        });
    } 

    // 주간, 일일 컨테이너 밑에 "생성" 버튼 추가
    const createButton = $('<button>').addClass('btn btn-primary btn-sm').text('+');
    createButton.click(() => {
        createTask(modeType);
    });
    
    taskList.append(weeklyContainer);
    taskList.append(dailyContainer);
    modeContainer.append(taskList);

    container.append(modeContainer);
}