import { createTaskElement, createAddTaskButton } from './taskListSettingUtils.js';

export function renderTasks(modeType, tasks, container) {
    const weeklyTasks = tasks.filter(task => task.frequencyType === 'WEEKLY');
    const dailyTasks = tasks.filter(task => task.frequencyType === 'DAILY');

    const modeContainer = $('<div>', { class: 'task-category' }).html(`<h2>${modeType}</h2>`);

    const taskList = $('<div>', { class: 'task-list' });
    // 주간 컨테이너 생성
    const weeklyContainer = $('<div>', {class: 'task-list-weekly', 'data-frequency-type': 'WEEKLY', 'data-mode-type': modeType}).html(`<h3>주간(WEEKLY)</h3>`);
    // 일일 컨테이너 생성
    const dailyContainer = $('<div>', {class: 'task-list-daily', 'data-frequency-type': 'DAILY', 'data-mode-type': modeType}).html(`<h3>일간(DAILY)</h3>`);

    // 주간 태스크 생성
    weeklyTasks.forEach(task => {
        const taskElement = createTaskElement(task);
        weeklyContainer.append(taskElement);
    });
    // 주간 태스크 추가 버튼 생성
    const weeklyAddButton = createAddTaskButton(weeklyContainer);
    weeklyContainer.append(weeklyAddButton);

    // 일일 태스크 생성
    dailyTasks.forEach(task => {
        const taskElement = createTaskElement(task);
        dailyContainer.append(taskElement);
    });
    // 일일 태스크 추가 버튼 생성
    const dailyAddButton = createAddTaskButton(dailyContainer);
    dailyContainer.append(dailyAddButton);

    taskList.append(weeklyContainer);
    taskList.append(dailyContainer);
    modeContainer.append(taskList);

    container.append(modeContainer);
}