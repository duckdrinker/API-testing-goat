import jwt from 'jsonwebtoken';

// ASEC-1402: jwt_misconfiguration in a .jsx file, no route/handler attached to it —
// only reachable via the content sweep (see #1347). Same trigger pattern as app.js's verifyToken().
export function useAuth(token) {
  const claims = jwt.verify(token, 'unused-key', { algorithms: ['none'] });
  return claims;
}
