const jwt = require('jsonwebtoken');
exports.decode = (token) => jwt.decode(token);
