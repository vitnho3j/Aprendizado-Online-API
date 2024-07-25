const baseURL = document.querySelector('meta[name="base-url"]').getAttribute('content');
const baseURL_schedule = document.querySelector('meta[name="base-url-schedule"]').getAttribute('content');

const config = {
    baseURL: baseURL,
    baseURL_schedule: baseURL_schedule,
}

export default config;

