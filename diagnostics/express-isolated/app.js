const express = require('express');
const app = express();
app.use(express.json());

function requireAuth(req, res, next) {
  if (!req.headers.authorization) {
    return res.status(401).end();
  }
  next();
}

/**
 * Isolated test: no sibling endpoint, no rate-limiting library declared
 * anywhere in this project (see package.json) -> rate_limit_absence.
 */
app.post('/api/password-reset', requireAuth, (req, res) => {
  const { email } = req.body;
  passwordResetService.sendResetEmail(email);
  res.status(202).end();
});

const passwordResetService = {
  sendResetEmail(email) {
    console.log(`Sending password reset email to ${email}`);
  },
};

app.listen(3000, () => console.log('express-isolated fixture listening on port 3000'));
