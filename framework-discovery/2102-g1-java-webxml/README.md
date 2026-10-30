# Gap 1 - servlets declared only in web.xml
Expected endpoints: GET/POST /setup; GET /multi and GET /multi/alt; DELETE /admin/purge (role admin from security-constraint).
Must NOT be an endpoint: servlets.Unmapped (no mapping).
