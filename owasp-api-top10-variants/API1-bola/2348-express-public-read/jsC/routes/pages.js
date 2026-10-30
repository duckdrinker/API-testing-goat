const Page = require('../models/page')

exports.read = () => async (req, res) => {
  const page = await Page.findOne({ where: { id: req.params.id } })
  res.json(page)
}

exports.update = () => async (req, res) => {
  const page = await Page.findOne({ where: { id: req.params.id } })
  await page.update({ title: req.body.title })
  res.json(page)
}
