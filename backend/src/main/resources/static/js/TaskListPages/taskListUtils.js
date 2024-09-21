export function createTaskElement(task) {
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

export function toggleTask(taskElement, taskId) {
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