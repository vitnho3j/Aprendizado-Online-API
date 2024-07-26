import config from "./config.js";

async function fetchNextExecutionTime() {
    try {
        const response = await fetch(`${config.baseURL_schedule}/schedule`);
        const timeToNextExecution = await response.json();
        return timeToNextExecution;
    } catch (error) {
        console.error('Error fetching the next execution time:', error);
    }
}

function startTimer(duration) {
    let timer = duration, hours, minutes, seconds;
    const timerElement = document.getElementById('timer');
    const h1Element = document.querySelector('.schedule');

    const interval = setInterval(() => {
        hours = parseInt(timer / 3600, 10);
        minutes = parseInt((timer % 3600) / 60, 10);
        seconds = parseInt(timer % 60, 10);

        hours = hours < 10 ? "0" + hours : hours;
        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        timerElement.textContent = `${hours}:${minutes}:${seconds}`;

        if (--timer < 0) {
            clearInterval(interval);
            let countdown = 5;
            const countdownInterval = setInterval(() => {
                h1Element.id = "reloading";
                h1Element.innerHTML = `A página será recarregada em: ${countdown} segundos`;
                countdown--;
                if (countdown < 0) {
                    clearInterval(countdownInterval);
                    window.location.reload();
                }
            }, 1000);

            fetchNextExecutionTime().then(timeToNextExecution => {
                timer = timeToNextExecution / 1000;
                startTimer(timer); // Reiniciar o timer com o novo valor
            });
        }
    }, 1000);
}

fetchNextExecutionTime().then(timeToNextExecution => {
    startTimer(timeToNextExecution / 1000);
});

// async function fetchNextExecutionTime() {
//     try {
//         const response = await fetch(`${config.baseURL_schedule}/schedule`);
//         const timeToNextExecution = await response.json();
//         return timeToNextExecution;
//     } catch (error) {
//         console.error('Error fetching the next execution time:', error);
//     }
// }

// function startTimer(duration) {
//     let timer = duration, hours, minutes, seconds;
//     const timerElement = document.getElementById('timer');

//     setInterval(() => {
//         hours = parseInt(timer / 3600, 10);
//         minutes = parseInt((timer % 3600) / 60, 10);
//         seconds = parseInt(timer % 60, 10);

//         hours = hours < 10 ? "0" + hours : hours;
//         minutes = minutes < 10 ? "0" + minutes : minutes;
//         seconds = seconds < 10 ? "0" + seconds : seconds;

//         timerElement.textContent = `${hours}:${minutes}:${seconds}`;

//         if (--timer < 0) {
//             loadUsers()
//             fetchNextExecutionTime().then(timeToNextExecution => {
//                 timer = timeToNextExecution / 1000;
//             });
//         }
//     }, 1000);
// }

// fetchNextExecutionTime().then(timeToNextExecution => {
//     startTimer(timeToNextExecution / 1000);
// });