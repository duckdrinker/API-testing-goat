const jwt = require('jsonwebtoken');
exports.decode = (token) => jwt.verify(token, process.env.JWT_SECRET);
