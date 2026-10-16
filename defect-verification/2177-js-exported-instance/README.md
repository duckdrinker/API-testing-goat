# Fixture #2177 - JS flow through a method of an exported instance (class not exported)

Each folder is an independent project. Scan: `xygeni scan --run=sast --dir <case> ...`.
Rule under test: `javascript.shared_mutable_state` (write `this.orders[number] = order` in `OrderStore.save`).

| Case | Export / import form | Expected `shared_mutable_state` on order-store.js |
|---|---|---|
| `a-cjs-shorthand` | `module.exports = { orderStore }` / destructured `require` | YES |
| `b-cjs-inline` | `module.exports = { orderStore: new OrderStore() }` / destructured `require` | YES |
| `c-cjs-exports-dot` | `exports.orderStore = new OrderStore()` / destructured `require` | YES |
| `d-esm` | `export const orderStore = new OrderStore()` / named `import` | YES |
| `e-per-request` | class exported, instance built per request (`new OrderStore().save(...)`) | NO (control) |
| `f-no-request-data` | instance exported, route passes constants only | NO (control) |
