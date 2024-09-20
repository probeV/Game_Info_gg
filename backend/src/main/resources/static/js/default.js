import { protectedApiEndpoints } from './auth'; // 파일 이름 변경

$(document).ajaxSend(function(event, xhr, settings) {
    // 권한이 필요한 API 요청인지 확인
    if (protectedApiEndpoints.some(endpoint => settings.url.includes(endpoint))) {
        // 요청이 전송되기 전에 authorization 헤더에 accessToken 추가
        const accessToken = localStorage.getItem('accessToken');
        if (accessToken) {
            xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
        }
    }
});

$(document).ajaxComplete(function(event, xhr, settings) {
    // 응답이 완료된 후 authorization 헤더에 accessToken 체크
    const newAccessToken = xhr.getResponseHeader('Authorization');
    if (newAccessToken && newAccessToken.startsWith('Bearer ')) {
        // 새로운 accessToken이 존재하면 localStorage에 저장
        localStorage.setItem('accessToken', newAccessToken.split(' ')[1]);
    }
});
