module.exports = {
    devServer: {
        proxy: {
            '/api/v1': {
                target: 'http://localhost:9003'
            },
            '/api/backend': {
                target: 'http://localhost:8080'
            },

            '/api/v2': {
                target: 'http://localhost:9004'
            }
        }
    }
}