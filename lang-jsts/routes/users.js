const express = require('express');

const router = express.Router();

// Mounted at /api/v2/users via require('./routes/users') inline in app.js.
// Path param from a plain ':id' segment.
router.get('/:id', (req, res) => {
  res.json({ id: req.params.id, name: 'Mounted User' });
});

// Request DataObject synthesized from req.body destructuring (PUT).
router.put('/:id', (req, res) => {
  const { name, email } = req.body;
  res.json({ id: req.params.id, name, email });
});

module.exports = router;
