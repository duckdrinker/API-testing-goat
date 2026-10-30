const express = require('express')
const security = require('./lib/security')
const { read, update } = require('./routes/pages')

const app = express()
app.use(express.json())
app.get('/api/pages/:id', security.isAuthenticated(), read())
app.put('/api/pages/:id', security.isAuthenticated(), update())
app.listen(3000)
