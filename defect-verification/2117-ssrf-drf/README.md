# Fixture #2117 - ssrf on DRF read-only `url`

| Endpoint | Expected `ssrf` |
|---|---|
| POST/PUT/PATCH /api/widgets/ (WidgetViewSet; `url` read-only self-link) | NONE |
| POST /api/labels/ (writable URL input, handler never fetches) | NONE |
| POST /api/notes/ (clean negative: URL input, no call named `get` in handler) | NONE |
| POST /api/imports/ (APIView, requests.get on user URL) | 1 finding, high |
| POST /api/imports-vs/ (ViewSet.serializer_class, requests.get) | 1 finding, high |
