# spec-only

Just an OpenAPI 3.x spec file (`openapi.yaml`, 2 endpoints under `/api/shipments`) — **no source code at all**, no
package manifest. Used by APIN-006 in `test-plan-apisec-inventory-synthesis.html`: with
`xygeni --run=inventory,apisecurity --dir fixtures/spec-only --spec fixtures/spec-only/openapi.yaml --upload`,
the inventory sub-scan should find no packages/APIs (no manifest, no framework code), while the apisec sub-scan
should still report the 2 endpoints ingested from the spec file.

Committed as a plain (unzipped) folder, same as the other fixtures added for this TP.
