import express from 'express'
import * as security from './lib/security'
import * as pages from './routes/pages'

const app = express()
app.use(express.json())
app.get('/api/pages/:id', security.isAuthenticated(), pages.read())
app.put('/api/pages/:id', security.isAuthenticated(), pages.update())
app.listen(3000)