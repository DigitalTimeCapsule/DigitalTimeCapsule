const config = {
    apiUrl: process.env.NODE_ENV === 'production'
        ? 'https://your-production-api-url.com/api'
        : 'http://localhost:8080/api'
};

export default config; 