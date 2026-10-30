import { Page } from '../models/page'

export const read = () => async (req: any, res: any) => {
  const page = await Page.findOne({ where: { id: req.params.id } })
  res.json(page)
}

export const update = () => async (req: any, res: any) => {
  const page = await Page.findOne({ where: { id: req.params.id } })
  await page.update({ title: req.body.title })
  res.json(page)
}