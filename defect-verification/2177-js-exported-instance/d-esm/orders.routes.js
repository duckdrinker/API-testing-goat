import express from 'express';
import { orderStore } from './order-store.js';
const router = express.Router();
router.post('/orders', (req, res) => { orderStore.save(req.body.number, req.body.order); res.sendStatus(201); });
export default router;
