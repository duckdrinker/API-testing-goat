'use strict';

function login(req, res) {
  const { user, pass } = req.body;
  res.json({ ok: user === 'a' && pass === 'b' });
}

module.exports = { login };
