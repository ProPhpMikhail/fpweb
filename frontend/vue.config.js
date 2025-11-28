module.exports = {
    devServer: {
        port: 8080,
        host: '0.0.0.0',
        allowedHosts: 'all',
        proxy: {
            '^/api': {
                target: 'http://backend-dev:8080',
                changeOrigin: true,
                secure: false,
            },
        },
    },
};