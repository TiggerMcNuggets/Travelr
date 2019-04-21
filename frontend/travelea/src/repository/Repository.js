import axios from "axios"

// Base URL used by all requests
const baseUrl = "http://localhost:9000/api";

// Create the axios object
const repo = axios.create({
    baseURL: baseUrl,
});

repo.defaults.headers.post["Access-Control-Allow-Origin"] = "*";

// Add the auth header if it exists
repo.interceptors.request.use(
    (config) => {
        let token = localStorage.getItem('token');

        if (token) {
            config.headers['X-Authorization'] = token;
        }

        return config;
    },

    (error) => {
        return Promise.reject(error);
    }
);

export default repo;
