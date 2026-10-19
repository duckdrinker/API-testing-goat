import express from 'express'
import fs from 'node:fs'
import { WalletModel } from './models/wallet'
import { BasketModel } from './models/basket'

const app = express()

app.post('/checkout/:id', (req, res, next) => {
  BasketModel.findOne({ where: { id: req.params.id } })
    .then(async (basket) => {
      const writer = fs.createWriteStream('out.pdf')
      writer.on('finish', () => {
        res.json({ ok: true })
      })
      await WalletModel.increment({ balance: req.body.points }, { where: { UserId: req.body.UserId } })
      writer.end()
    })
    .catch((error) => next(error))
})