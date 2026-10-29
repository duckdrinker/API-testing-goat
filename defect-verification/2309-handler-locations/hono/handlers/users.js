export function listUsers(c) {
  const users = [];
  return c.json(users);
}
