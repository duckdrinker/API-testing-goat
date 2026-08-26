const express = require('express');

const router = express.Router();

// Mounted at /api/v2/admin via a variable in app.js
// (const adminRoutes = require('./routes/admin'); app.use('/api/v2/admin', adminRoutes)).
// Path param from an optional ':id?' segment.
router.get('/:id?', (req, res) => {
  res.json({ id: req.params.id || null, role: 'admin' });
});

router.post('/:id/reset', (req, res) => {
  res.status(204).end();
});

module.exports = router;
