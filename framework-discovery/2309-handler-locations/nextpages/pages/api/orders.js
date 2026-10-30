// orders API

const helper = () => 1;

export default async function ordersHandler(req, res) {
  if (req.method === 'POST') {
    return res.status(201).json({});
  }
  res.status(200).json([]);
}
