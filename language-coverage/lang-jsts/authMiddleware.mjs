import jwt from 'jsonwebtoken';

// ASEC-1402: jwt_misconfiguration in a .mjs file, no route/handler attached to it —
// only reachable via the content sweep (see #1347), which is exactly what #1402 says gets
// swept then discarded by the extension filter. Same trigger pattern as app.js's verifyToken().
export function verifyToken(token) {
  return jwt.verify(token, 'unused-key', { algorithms: ['none'] });
}
