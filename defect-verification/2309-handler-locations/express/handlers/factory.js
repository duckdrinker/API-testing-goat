'use strict';

function makeHandler() {
  return function (req, res) {
    res.json({ made: true });
  };
}

module.exports = { makeHandler };
