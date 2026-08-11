const express = require('express');
const cors = require('cors');
const jwt = require('jsonwebtoken');

const app = express();
app.use(express.json());

function requireAuth(req, res, next) {
  if (!req.headers.authorization) return res.status(401).end();
  req.user = { id: req.headers['x-user-id'] || 'demo-user' };
  next();
}

function requireRole(role) {
  return (req, res, next) => {
    if (req.headers['x-user-role'] !== role) return res.status(403).end();
    next();
  };
}

// ASEC-058 / ASEC-066: excessive_data_exposure + pii_leak_in_response positive.
// "ssn" is never referenced by the request and is sensitivity-tagged.
app.get('/api/users/profile', (req, res) => {
  const { ssn } = req.query;
  res.json({ id: '1', name: 'Jane Doe', email: 'jane@example.com', ssn });
});

// ASEC-059 / ASEC-067: negative — only operation-relevant fields exposed.
app.get('/api/users/:id/summary', requireAuth, (req, res) => {
  res.json({ id: req.params.id, name: 'Jane Doe' });
});

// ASEC-095: mass_assignment positive — "role" binds through to the persisted user.
app.put('/api/users/profile', (req, res) => {
  const { name, email, role } = req.body;
  res.json({ id: '1', name, email, role });
});

// mass_assignment negative — no bindable protected field.
app.put('/api/users/:id/safe-update', requireAuth, (req, res) => {
  const { name, email } = req.body;
  res.json({ id: req.params.id, name, email });
});

// ASEC-071: BOLA Phase B, check absent.
app.get('/api/products/:id', requireAuth, (req, res) => {
  res.json({ id: req.params.id });
});

// ASEC-071: BOLA Phase B, check present — req.user.id === obj.userId.
app.get('/api/safe-products/:id', requireAuth, (req, res) => {
  const product = { id: req.params.id, userId: req.user.id };
  if (req.user.id === product.userId) {
    res.json(product);
  } else {
    res.status(403).end();
  }
});

// ASEC-076: BFLA Phase B, check absent — /admin/ path, generic auth only.
app.post('/api/admin/users/:id/lock', requireAuth, (req, res) => {
  res.status(204).end();
});

// ASEC-076: BFLA Phase B, check present — requireRole('admin') middleware in the chain.
app.post('/api/admin/users/:id/unlock', requireAuth, requireRole('admin'), (req, res) => {
  res.status(204).end();
});

// ASEC-079: cors_misconfiguration — app.use(cors()) with no options -> wildcard default.
app.use('/api/public', cors());
app.get('/api/public/status', (req, res) => {
  res.json({ status: 'ok' });
});

// ASEC-088: jwt_misconfiguration — algorithms: ['none'] accepted.
function verifyToken(token) {
  return jwt.verify(token, 'unused-key', { algorithms: ['none'] });
}

app.listen(3000, () => console.log('lang-jsts fixture listening on port 3000'));
