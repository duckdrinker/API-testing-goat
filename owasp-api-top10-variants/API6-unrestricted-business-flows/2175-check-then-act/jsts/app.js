const express = require('express');
const { Sequelize, DataTypes } = require('sequelize');

const sequelize = new Sequelize('sqlite::memory:');
const Coupon = sequelize.define('Coupon', { code: DataTypes.STRING, status: DataTypes.STRING });

const app = express();
app.use(express.json());

// c2 helper: write lives in another function
async function markUsed(coupon) {
  coupon.status = 'used';
  await coupon.save();
}

// c1: unguarded check -> write
app.post('/coupons/:id/redeem-unguarded', async (req, res) => {
  const coupon = await Coupon.findByPk(req.params.id);
  if (coupon.status === 'open') {
    coupon.status = 'used';
    await coupon.save();
  }
  res.sendStatus(200);
});

// c2: check in handler, write in helper
app.post('/coupons/:id/redeem-split', async (req, res) => {
  const coupon = await Coupon.findByPk(req.params.id);
  if (coupon.status === 'open') {
    await markUsed(coupon);
  }
  res.sendStatus(200);
});

// c3: atomic conditional UPDATE
app.post('/coupons/:id/redeem-atomic', async (req, res) => {
  const coupon = await Coupon.findByPk(req.params.id);
  if (coupon.status === 'open') {
    await sequelize.query("UPDATE Coupons SET status = 'used' WHERE id = ? AND status = 'open'", { replacements: [coupon.id] });
  }
  res.sendStatus(200);
});

// c4: delete of the row just checked
app.post('/coupons/:id/delete-after-check', async (req, res) => {
  const coupon = await Coupon.findByPk(req.params.id);
  if (coupon.status === 'expired') {
    await coupon.destroy();
  }
  res.sendStatus(200);
});

// c6: already-done write
app.post('/coupons/:id/already-done-write', async (req, res) => {
  const coupon = await Coupon.findByPk(req.params.id);
  if (coupon.status === 'used') {
    coupon.status = 'used';
    await coupon.save();
  }
  res.sendStatus(200);
});

app.listen(3000);
