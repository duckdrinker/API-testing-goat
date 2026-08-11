# Diagnostic fixture — DRF `as_view()` method resolution

**Related to:** ASEC-101 (rate_limit_absence, Python/DRF)

## Why this fixture exists

While investigating why `rate_limit_absence` never fires on `fixtures/lang-python`'s
`SupportTicketDrfView` (a DRF class-based view with `permission_classes = [IsAuthenticated]`, a
`post()` method, and no `throttle_classes` — the textbook positive case for ASEC-101), we first
suspected the sibling `FeedbackView` (which does declare `throttle_classes`) was suppressing the
finding project-wide. This fixture isolates `SupportTicketDrfView` alone, with no other view in
the project, to rule that out.

## What it shows

Even fully isolated, `rate_limit_absence` still doesn't fire (0 flaws). Inspecting the discovered
endpoints explains why — there are two separate entries for the same view, and neither has both
the correct path and the correct method:

```
POST /                              <- from drf_views.py's own analysis: correct method, generic path
GET /api/support-tickets-drf/       <- from urls.py: correct path, WRONG method (should be POST)
```

`SupportTicketDrfView` only defines `post()`, never `get()`. When Django resolves the view via
`SupportTicketDrfView.as_view()` in `urls.py` (which doesn't specify the method explicitly — that's
determined by the view class itself), the discovery engine doesn't cross-reference back to the
class's actually-defined HTTP methods and defaults to GET instead.

This connects to a separately-confirmed behaviour (see `fixtures/low-severity-only`): a project
with only GET endpoints does not trigger `rate_limit_absence` at all. Since this endpoint is
misclassified as GET, it falls into that same "GET-only project" case — which is the likely real
root cause for ASEC-101's failure, rather than a throttle-scope issue.

## Expected scan result

```
xygeni apisecurity --dir fixtures/drf-isolated -o out.json -f json
```

`rate_limit_absence` does not fire (confirming the bug); the endpoint list shows the
path/method mismatch described above.
