class OrderStore {
  constructor() { this.orders = {}; }
  save(number, order) { this.orders[number] = order; }
}
module.exports = { orderStore: new OrderStore() };
