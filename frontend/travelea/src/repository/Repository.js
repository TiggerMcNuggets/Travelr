import axios from "axios"

// Base URL used by all requests
const baseUrl = "http://localhost:9000"

// Create the axios object
const repo = axios.create({
    baseURL: baseUrl,
    headers: {
        "X-Authorization": "123"
    }
});

// Add the auth header if it exists
repo.interceptors.request.use(
    (config) => {
        let token = localStorage.getItem('token');

        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }

        return config;
    },

    (error) => {
        return Promise.reject(error);
    }
)

export default repo;