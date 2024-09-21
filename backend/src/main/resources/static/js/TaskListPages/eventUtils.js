export function getEventMessage(event) {
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