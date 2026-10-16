# Fixture #2263 - a file named action.yml that is not a GitHub Action

Scan: `xygeni scan --run=misconf --dir <case> ...` (also compliance and malware log the same file).

| Case | Content | Expected |
|---|---|---|
| `a-list-named-action-yml` | YAML list of DB rows (gitea `models/fixtures/action.yml`) | no parse error |
| `b-list-named-action-yaml` | same, `.yaml` extension | no parse error |
| `c-mapping-without-runs` | mapping without `runs` | no parse error |
| `d-valid-action` | valid composite action, unpinned `actions/checkout@main` | parsed (unpinned-uses finding), no parse error |
| `e-malformed-action` | has `runs` but invalid YAML | parse error (still reported) |
| `f-mixed` | a + d + e together | parse error only for the malformed action |
