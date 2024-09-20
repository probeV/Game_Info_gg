$(document).ready(function() {
    let adminActiveTokens = ["/admin/*"];
    let userActiveTokens = ["/users/*"];
    let authorities;

    // JWT 토큰에서 보호된 엔드포인트 목록과 권한을 가져옴
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
        const payload = JSON.parse(atob(accessToken.split('.')[1]));
        authorities = payload.auth ? payload.auth : null;
        console.log(authorities);
    }

    // 해당 페이지(URI)에 접근할 때 실행
    $(document).ajaxSend(function(event, xhr, settings) {
        const requestURI = window.location.pathname;
        const settingsURI = settings.url;

        console.log(requestURI);
        console.log(settingsURI);


        const isAdminActive = adminActiveTokens.some(token => requestURI.startsWith(token.replace('*', '')));
        const isUserActive = userActiveTokens.some(token => requestURI.startsWith(token.replace('*', '')));

        // 권한이 필요없는 페이지
        if(!isAdminActive && !isUserActive){
            console.log("권한이 필요없는 요청");
            return;
        }

        // 권한이 필요한 페이지인데, accessToken이 없으면 재로그인
        if(!accessToken){
            alert("로그인 해주세요");
            window.location.href = "/login";
            return;
        }

        // 현재 페이지가 유저 전용, 권한이 있는지 체크
        if(isUserActive && authorities === "USER"){
            console.log("USER 권한이 필요한 요청");
            xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
            return;
        }
        else if(isUserActive){
            console.log("USER 권한이 필요한 요청");
            alert("권한이 없습니다.");
            window.location.href = "/";
            return;
        }


        // 현재 페이지가 관리자 전용, 권한이 있는지 체크
        if(isAdminActive && authorities === "ADMIN"){
            console.log("ADMIN 권한이 필요한 요청");
            xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
            return;
        }
        else if(isAdminActive){
            console.log("ADMIN 권한이 필요한 요청");
            alert("권한이 없습니다.");
            window.location.href = "/";
            return;
        }

        // 로그인 오류
        alert("권한 오류 : 재로그인 해주세요");
        window.location.href = "/login";

    });
});

