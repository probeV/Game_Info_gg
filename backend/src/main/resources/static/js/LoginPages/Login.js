// 클라이언트에서 AccessToken을 저장하고 리다이렉트하는 코드
function fetchAccessTokenAndRedirect() {
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get('code');
    const state = urlParams.get('state');

    fetch(`http://localhost:8080/login/oauth2/code/naver?code=${code}&state=${state}`, {
        method: "GET"
    })
    .then(response => {
        if (response.ok) {
            // AccessToken을 헤더에서 꺼내기
            const accessToken = response.headers.get("Authorization").split(" ")[1];
            // AccessToken을 localStorage에 저장
            localStorage.setItem("accessToken", accessToken);
            // "/"로 리다이렉트
            window.location.href = "/";
        } else {
            console.error("Failed to fetch the resource");
        }
    })
    .catch(error => {
        console.error("Error:", error);
    });
}

// 함수 호출
fetchAccessTokenAndRedirect();