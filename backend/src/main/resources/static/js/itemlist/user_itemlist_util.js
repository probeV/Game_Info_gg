// 남은 시간 타이머
export default function startResetTimeTimer() {
    setInterval(function() {
        $('.item_reset_time').each(function() {
            const currentTime = new Date().getTime();
            const resetTimeMillis = new Date($(this).data('reset-time')).getTime();
            const timeDifference = resetTimeMillis - currentTime;

            if (timeDifference > 0) {
                const hours = Math.floor(timeDifference / (1000 * 60 * 60));
                const minutes = Math.floor((timeDifference % (1000 * 60 * 60)) / (1000 * 60));
                const seconds = Math.floor((timeDifference % (1000 * 60)) / 1000);
                const formattedHours = hours.toString().padStart(2, '0');
                const formattedMinutes = minutes.toString().padStart(2, '0');
                const formattedSeconds = seconds.toString().padStart(2, '0');
                $(this).text(`${formattedHours}:${formattedMinutes}:${formattedSeconds}`);
            } else {
                $(this).text('00:00:00');
            }

            if (timeDifference < 24 * 60 * 60 * 1000) {
                changeResetTimeColor($(this));
            }
        });
    }, 1000);
}

// 남은 시간이 24시간 이하이면 남은 시간 붉은 색으로 변경
function changeResetTimeColor(element) {
    element.css('color', 'red');
}


