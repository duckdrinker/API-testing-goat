class OrderStore {
  constructor() { this.orders = {}; }
  save(number, order) { this.orders[number] = order; }
}
const orderStore = new OrderStore();
module.exports = { orderStore };
