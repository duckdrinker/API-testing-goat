# #2234 / bug #2367 - async IIFE inside a callback makes the money detectors fail with `null`

Two identical Express/Sequelize handlers; the only difference is the `void (async () => { ... })()` wrapper
inside the `finish` callback. `models/` is copied from OWASP Juice Shop (MIT).

| Fixture | Expected (fixed) | Seen in 6.21.0 |
|---|---|---|
| `m1-iife` | no WARN; `financial_write_without_idempotency: 1 of 1 handlers that write move money` | 2 WARN (`check_then_act_without_guard` on `POST /checkout/{id}`, `financial_write_without_idempotency` scan-scope), both `null`; no selectivity line for idempotency |
| `m2-plain` (control) | same | OK: `1 of 1 handlers that write move money` |

Scan: `xygeni scan --run=apisec --dir <fixture> --no-upload --never-fail` (PowerShell), then read `apisec_*.log`.