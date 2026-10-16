class OrderStore {
  constructor() { this.orders = {}; }
  save(number, order) { this.orders[number] = order; }
}
export const orderStore = new OrderStore();
