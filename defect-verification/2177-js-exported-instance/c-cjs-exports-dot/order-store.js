class OrderStore {
  constructor() { this.orders = {}; }
  save(number, order) { this.orders[number] = order; }
}
exports.orderStore = new OrderStore();
