export const isAuthenticated = () => (req: any, res: any, next: any) => {
  if (!req.headers.authorization) return res.status(401).end()
  next()
}