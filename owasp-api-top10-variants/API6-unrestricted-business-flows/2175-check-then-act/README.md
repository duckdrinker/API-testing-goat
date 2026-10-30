# 2175 check_then_act_without_guard - cross-language fixtures

Expected verdicts written from the fixture source BEFORE scanning (CLI 6.22.0). Entity: Coupon(status). Check = read of the row + comparison on status; act = write.

Case ids: c1 unguarded; c2 check->write across methods (controller->service/helper); c3 conditional SQL write; c4 delete of the checked row; c5a same lock; c5b different lock blocks; c5c unlock before write; c6 already-done write (check `status == used`, write sets `used`); c7 new row after existence check.

| language | endpoint | expected | reason |
|---|---|---|---|
| java | POST /coupons/{id}/redeem-unguarded | FINDING | c1: read, compare status, write, no guard |
| java | POST /coupons/{id}/redeem-split | FINDING | c2: check in handler, write in another method of same request |
| java | POST /coupons/{id}/redeem-atomic | NO FINDING | c3: write is UPDATE ... WHERE status='open' (conditional) |
| java | POST /coupons/{id}/delete-after-check | NO FINDING | c4: only write is delete of the row just checked |
| java | POST /coupons/{id}/already-done-write | NO FINDING | c6: write re-sets state the check already saw (already done) |
| java | POST /coupons/{id}/sync-same-block | NO FINDING | c5a: check and write inside the same lock block |
| java | POST /coupons/{id}/sync-different-blocks | FINDING | c5b: check in one lock block, write in another |
| java | POST /coupons/{id}/unlock-before-write | FINDING | c5c: lock released before the write |
| csharp | POST /coupons/{id}/redeem-unguarded | FINDING | c1: read, compare status, write, no guard |
| csharp | POST /coupons/{id}/redeem-split | FINDING | c2: check in handler, write in another method of same request |
| csharp | POST /coupons/{id}/redeem-atomic | NO FINDING | c3: write is UPDATE ... WHERE status='open' (conditional) |
| csharp | POST /coupons/{id}/delete-after-check | NO FINDING | c4: only write is delete of the row just checked |
| csharp | POST /coupons/{id}/already-done-write | NO FINDING | c6: write re-sets state the check already saw (already done) |
| csharp | POST /coupons/{id}/lock-same-block | NO FINDING | c5a: check and write inside the same lock block |
| csharp | POST /coupons/{id}/lock-different-blocks | FINDING | c5b: check in one lock block, write in another |
| csharp | POST /coupons/{id}/unlock-before-write | FINDING | c5c: lock released before the write |
| kotlin | POST /coupons/{id}/redeem-unguarded | FINDING | c1: read, compare status, write, no guard |
| kotlin | POST /coupons/{id}/redeem-split | FINDING | c2: check in handler, write in another method of same request |
| kotlin | POST /coupons/{id}/redeem-atomic | NO FINDING | c3: write is UPDATE ... WHERE status='open' (conditional) |
| kotlin | POST /coupons/{id}/delete-after-check | NO FINDING | c4: only write is delete of the row just checked |
| kotlin | POST /coupons/{id}/already-done-write | NO FINDING | c6: write re-sets state the check already saw (already done) |
| kotlin | POST /coupons/{id}/sync-same-block | NO FINDING | c5a: check and write inside the same lock block |
| kotlin | POST /coupons/{id}/sync-different-blocks | FINDING | c5b: check in one lock block, write in another |
| go | POST /coupons/{id}/redeem-unguarded | FINDING | c1: read, compare status, write, no guard |
| go | POST /coupons/{id}/redeem-split | FINDING | c2: check in handler, write in another method of same request |
| go | POST /coupons/{id}/redeem-atomic | NO FINDING | c3: write is UPDATE ... WHERE status='open' (conditional) |
| go | POST /coupons/{id}/delete-after-check | NO FINDING | c4: only write is delete of the row just checked |
| go | POST /coupons/{id}/already-done-write | NO FINDING | c6: write re-sets state the check already saw (already done) |
| jsts | POST /coupons/{id}/redeem-unguarded | FINDING | c1: read, compare status, write, no guard |
| jsts | POST /coupons/{id}/redeem-split | FINDING | c2: check in handler, write in another method of same request |
| jsts | POST /coupons/{id}/redeem-atomic | NO FINDING | c3: write is UPDATE ... WHERE status='open' (conditional) |
| jsts | POST /coupons/{id}/delete-after-check | NO FINDING | c4: only write is delete of the row just checked |
| jsts | POST /coupons/{id}/already-done-write | NO FINDING | c6: write re-sets state the check already saw (already done) |
| python | POST /coupons/{id}/redeem-unguarded | FINDING | c1: read, compare status, write, no guard |
| python | POST /coupons/{id}/redeem-split | FINDING | c2: check in handler, write in another method of same request |
| python | POST /coupons/{id}/redeem-atomic | NO FINDING | c3: write is UPDATE ... WHERE status='open' (conditional) |
| python | POST /coupons/{id}/delete-after-check | NO FINDING | c4: only write is delete of the row just checked |
| python | POST /coupons/{id}/already-done-write | NO FINDING | c6: write re-sets state the check already saw (already done) |
| python | POST /coupons/{code}/register-redemption | FINDING | c7: Redemption saved after filter_by(code).first() is None |
| php | POST /coupons/{id}/redeem-unguarded | FINDING | c1: read, compare status, write, no guard |
| php | POST /coupons/{id}/redeem-split | FINDING | c2: check in handler, write in another method of same request |
| php | POST /coupons/{id}/redeem-atomic | NO FINDING | c3: write is UPDATE ... WHERE status='open' (conditional) |
| php | POST /coupons/{id}/delete-after-check | NO FINDING | c4: only write is delete of the row just checked |
| php | POST /coupons/{id}/already-done-write | NO FINDING | c6: write re-sets state the check already saw (already done) |
| ruby | POST /coupons/{id}/redeem_unguarded | FINDING | c1: read, compare status, write, no guard |
| ruby | POST /coupons/{id}/redeem_split | FINDING | c2: check in handler, write in another method of same request |
| ruby | POST /coupons/:id/redeem_helper | FINDING | c2b: write in private helper of same controller |
| ruby | POST /coupons/{id}/redeem_atomic | NO FINDING | c3: write is UPDATE ... WHERE status='open' (conditional) |
| ruby | POST /coupons/{id}/delete_after_check | NO FINDING | c4: only write is delete of the row just checked |
| ruby | POST /coupons/{id}/already_done_write | NO FINDING | c6: write re-sets state the check already saw (already done) |
| swift | POST /coupons/{id}/redeem-unguarded | FINDING | c1: read, compare status, write, no guard |
| swift | POST /coupons/{id}/redeem-split | FINDING | c2: check in handler, write in another method of same request |
| swift | POST /coupons/{id}/redeem-atomic | NO FINDING | c3: write is UPDATE ... WHERE status='open' (conditional) |
| swift | POST /coupons/{id}/delete-after-check | NO FINDING | c4: only write is delete of the row just checked |
| swift | POST /coupons/{id}/already-done-write | NO FINDING | c6: write re-sets state the check already saw (already done) |

Note on c6 (already-done): the exact reference semantics is not documented; interpretation is the fixture above. Result there is judged as stated here, but flagged if ambiguous.
Not covered: unlockCalls / successStatuses rule config; Java synchronized+lock cases in Go/JS/Python/PHP/Ruby/Swift (no language lock construct used).

Variant added after the first scan (PHP/Ruby/Swift returned 0 findings for c1): `redeem-by-code` / `redeem_by_code` (c1v) = FINDING; find_by/where()->first()/query().first() read, status compare, save/update write.
