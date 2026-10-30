const express = require('express')
const security = require('./lib/security')
const pages = require('./routes/pages')

const app = express()
app.use(express.json())
app.get('/api/pages/:id', security.isAuthenticated(), pages.read())
app.put('/api/pages/:id', security.isAuthenticated(), pages.update())
app.listen(3000)
