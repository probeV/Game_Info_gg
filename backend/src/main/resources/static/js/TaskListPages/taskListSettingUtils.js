export function createTaskElement(task) {

    const updateContainer = $('<div>', {class: 'update-task-container'}); // 새로운 div 추가

    const taskElement = $('<div>', {class: 'task', 'data-id': task.id, 'data-name': task.name, 'data-event-type': task.eventType});

    // eventType 드롭다운 추가
    const eventTypeSelect = $('<select>', {style: 'width: 25%; height: 100%; margin-left: 3%;'});
    ['NORMAL', 'TIME', 'SPECIAL'].forEach(type => {
        eventTypeSelect.append($('<option>', {value: type, text: type}));
    });
    eventTypeSelect.val(task.eventType);
    taskElement.append(eventTypeSelect); 
    // 이름 수정 입력 필드 추가
    const nameInput = $('<input>', {type: 'text', style: 'width: 70%;'});
    nameInput.val(task.name);
    taskElement.append(nameInput); 

    updateContainer.append(taskElement); 

    // 삭제 버튼 추가
    const deleteButton = $('<button>', {type: 'button', style: 'font-size: 12px; background: none; border: none;'});
    deleteButton.text('x'); // 텍스트 크기 줄이기
    deleteButton.click(() => {
        taskElement.remove();
        updateContainer.remove();
    });
    updateContainer.append(deleteButton); // taskContainer에 추가

    return updateContainer;
}
