const express = require('express');
const rateLimit = require('express-rate-limit');
const app = express();
app.use(express.json());

function requireAuth(req, res, next) {
  if (!req.headers.authorization) {
    return res.status(401).end();
  }
  next();
}

/**
 * API4:2023 - Unrestricted Resource Consumption fixture.
 *
 * POST /api/password-reset is authenticated and accepts user input (email),
 * but there is no express-rate-limit / rate-limiter-flexible middleware
 * anywhere in its chain (or anywhere in this app) -> rate_limit_absence,
 * severity High (write endpoint).
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

/**
 * rate_limit_absence negative fixture.
 *
 * Same shape as /api/password-reset (authenticated, write, user input),
 * but wires express-rate-limit -> should NOT produce a finding.
 */
const feedbackLimiter = rateLimit({ windowMs: 15 * 60 * 1000, max: 100 });

app.post('/api/feedback', requireAuth, feedbackLimiter, (req, res) => {
  const { message } = req.body;
  feedbackService.submit(message);
  res.status(202).end();
});

const feedbackService = {
  submit(message) {
    console.log(`Feedback received: ${message}`);
  },
};

app.listen(3000, () => console.log('API4 fixture listening on port 3000'));
