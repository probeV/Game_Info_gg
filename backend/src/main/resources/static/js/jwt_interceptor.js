$(document).ready(function() {
    let adminPages = ["/admin/*"];
    let userPages = ["/user/*"];
    let activeTokenAPI = ["/api/v1/users/*", "/api/v1/admins/*"];

    let authorities;

    // JWT 토큰에서 보호된 엔드포인트 목록과 권한을 가져옴
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
        const payload = JSON.parse(atob(accessToken.split('.')[1]));
        authorities = payload.auth ? payload.auth : null;
    }

    // 모든 AJAX 요청에 Authorization 헤더 추가
    $.ajaxSetup({
        beforeSend: function(xhr, settings) {
            const settingsURI = settings.url;
            const isActiveTokenAPI = activeTokenAPI.some(token => settingsURI.startsWith(token.replace('*', '')));

            if (isActiveTokenAPI && accessToken) {
                //console.log("AUTH API");
                xhr.setRequestHeader('Authorization', 'Bearer ' + accessToken);
            } else if (isActiveTokenAPI) {
                //console.log("토큰이 없음 : 로그인 필요");
                window.location.href = "/login";
                return false; // 요청 중단
            } else {
                //console.log("COMMON API");
            }
        }
    });

    // 해당 페이지에 접근 가능한지 체크
    $(document).ajaxStart(function() {
        const requestURI = window.location.pathname;

        const isAdminActive = adminPages.some(token => requestURI.startsWith(token.replace('*', '')));
        const isUserActive = userPages.some(token => requestURI.startsWith(token.replace('*', '')));

        // 권한이 필요없는 페이지

        if(!isAdminActive && !isUserActive){
            //console.log("권한이 필요없는 페이지");
            return;
        }

        // 권한이 필요한 페이지인데, accessToken이 없으면 재로그인
        if(!accessToken){
            alert("로그인 해주세요");
            window.location.href = "/login";
            return;
        }

        // 현재 페이지가 유저 전용, 권한이 있는지 체크
        if(isUserActive && (authorities === "USER" || authorities === "ADMIN")){
            //console.log("USER 권한이 필요한 페이지 : 유저 권한 있음");
            return;
        }
        else if(isUserActive){
            //console.log("USER 권한이 필요한 페이지 : 유저 권한 없음");
            alert("권한이 없습니다.");
            window.location.href = "/";

            return;
        }


        // 현재 페이지가 관리자 전용, 권한이 있는지 체크
        if(isAdminActive && authorities === "ADMIN"){
            //console.log("ADMIN 권한이 필요한 페이지 : 관리자 권한 있음");
            return;
        }
        else if(isAdminActive){
            //console.log("ADMIN 권한이 필요한 페이지 : 관리자 권한 없음");
            alert("권한이 없습니다.");
            window.location.href = "/";

            return;
        }

        // 로그인 오류
        alert("권한 오류 : 재로그인 해주세요");
        window.location.href = "/login";
    });

    // ajax 요청 오류 처리
    $(document).ajaxError(function(event, xhr, settings, error) {
        if(xhr.status === 400){
            console.log("400 오류 : 토큰 재발급 필요");
            alert("권한이 만료되었습니다. 권한을 다시 받아옵니다.");

            // 쿠키에서 AccessToken을 읽어 로컬 스토리지에 저장하고 쿠키를 삭제
            function getCookie(name) {
                let value = "; " + document.cookie;
                let parts = value.split("; " + name + "=");

                if (parts.length === 2) return parts.pop().split(";").shift();
            }

            const accessToken = getCookie('AccessToken');

            console.log("accessToken : " + accessToken);
            if (accessToken) {
                localStorage.setItem('accessToken', accessToken);
                // 쿠키 삭제
                document.cookie = "AccessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
            } else {
                //console.error('AccessToken이 없습니다.');
            }
            location.reload();
            return;
        }
        if(xhr.status === 401){
            console.log("401 오류 : 재로그인 필요");
            alert("권한 오류, 로그인 해주세요.");
            window.location.href = "/login";
            return;

        }
        else if(xhr.status === 403){
            console.log("403 오류 : 권한이 없음");
            alert("권한이 없습니다.");
            window.location.href = "/";
            return;
        }
    });


    // jwt_intercepter 설정 완료 이벤트 트리거
    $(document).trigger('jwtIntercepterReady');
});

