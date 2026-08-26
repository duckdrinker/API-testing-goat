const jwt = require('jsonwebtoken');

// ASEC-1402: jwt_misconfiguration in a .cjs file, no route/handler attached to it —
// only reachable via the content sweep (see #1347). Same trigger pattern as app.js's verifyToken().
function verifyToken(token) {
  return jwt.verify(token, 'unused-key', { algorithms: ['none'] });
}

module.exports = { verifyToken };
