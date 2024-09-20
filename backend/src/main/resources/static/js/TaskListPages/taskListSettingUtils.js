export function createTaskElement(task) {
    const taskElement = $('<div>', {class: 'task', 'data-id': task.id});

    // 이름 수정 입력 필드 추가
    const nameInput = $('<input>', {type: 'text', value: task.name});
    taskElement.append(nameInput);

    // eventType 드롭다운 추가
    const eventTypeSelect = $('<select>');
    ['NORMAL', 'TIME', 'SPECIAL'].forEach(type => {
        eventTypeSelect.append($('<option>', {value: type, text: type}));
    });
    eventTypeSelect.val(task.eventType);
    taskElement.append(eventTypeSelect);

    return taskElement;
}
