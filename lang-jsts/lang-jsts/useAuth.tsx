import jwt from 'jsonwebtoken';

// ASEC-1402: jwt_misconfiguration in a .tsx file, no route/handler attached to it —
// only reachable via the content sweep (see #1347). Same trigger pattern as app.js's verifyToken().
// This is the exact example filename the ticket itself uses ("useAuth.tsx").
export function useAuth(token: string) {
  const claims = jwt.verify(token, 'unused-key', { algorithms: ['none'] });
  return claims;
}
