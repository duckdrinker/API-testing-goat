'use strict';

class Svc {
  constructor(db) {
    this.db = db;
  }

  handle(req, res) {
    res.json({ svc: true });
  }
}

module.exports = Svc;
