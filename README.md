# API-testing-goat

Colección de proyectos fixture usados para probar la feature **ApiSec (API Security)** de
Xygeni: el escáner `apisecurity` (12 detectores OWASP API Security Top 10 2023 + FlawCorrelator),
la capa de **API Discovery** (detección de frameworks/rutas/parámetros) y la **síntesis de
inventario de APIs** (sincronización entre el scan de inventory y el report de apisec).

Estos fixtures viven fuera de cualquier repo de test plans — cada subcarpeta de primer nivel
respalda uno o más Test Plans de `xygeni/DepsDoctor-Test` (`TestCases/10.ApiSec/`). No modificar
el contenido de un fixture sin comprobar antes a qué caso(s) de qué TP da soporte (ver el
`README.md` de cada fixture individual).

## Subcarpetas

### `owasp-api-top10/`

Un proyecto por categoría del OWASP API Security Top 10 2023 (`API1-bola` … `API10-unsafe-consumption-of-apis`).
Cada uno dispara (o documenta que no aplica, si el detector no existe) el detector Xygeni
correspondiente a esa categoría. Fixtures base del **TP Scanner** (`test-plan-apisec-scanner.html`,
prefijo `ASEC-*`).

### `language-coverage/`

Un proyecto por lenguaje/framework (`lang-csharp`, `lang-go`, `lang-jsts`, `lang-php`,
`lang-python`) que agrupa **todos** los detectores que aplican en ese lenguaje (excessive
data exposure, pii leak, BOLA/BFLA Fase B, CORS, JWT, mass assignment, rate limit ausente) en
un solo proyecto, en vez de una carpeta por detector×lenguaje. Completa la matriz de cobertura
por lenguaje del **TP Scanner** (`ASEC-*`).

### `framework-discovery/`

Cobertura de la capa de **API Discovery** (detección de framework, rutas, parámetros y
verbos HTTP a partir del código fuente), independiente de los detectores de seguridad:
- `lang-fastify`, `lang-hono`, `lang-jaxrs`, `lang-koa`, `lang-nestjs` — un proyecto por
  framework soportado, ejercitando sus formas de definir rutas.
- `autodiscovery-*` (confidence-boundary, fastapi-only, mixed-signals, no-manifest,
  unsupported-only) — casos diagnóstico del proceso de **autodiscovery** (`FrameworkProbe`
  / `DependencyFileProbe` / `AutodiscoveryConfig`) que decide qué framework(s) analizar.

Fixtures del **TP `APID-*`** (`test-plan-apisec-framework-discovery.html`, épica
`xygeni/product-backlog#4017`).

### `diagnostics/`

Casos diagnóstico puntuales, cada uno centrado en un mecanismo concreto del escáner en vez de
en una categoría OWASP:
- `auth-confidence-threshold` — matriz de confianza del detector `unauthenticated_endpoint`.
- `correlator` — las 3 reglas del `FlawCorrelator` (post-proceso sobre los hallazgos).
- `drf-isolated` / `express-isolated` — `rate_limit_absence` en un endpoint aislado (sin ruta
  hermana con rate limit) en Python/DRF y JS/Express respectivamente.
- `spec-formats` — ingestión del flag `--spec` en los 3 formatos soportados (OpenAPI 3.x,
  Swagger 2.0, Postman v2.1).

Fixtures del **TP Scanner** (`ASEC-*`).

### `inventory-synthesis/`

Fixtures del TP de **síntesis de inventario de APIs** (`test-plan-apisec-inventory-synthesis.html`,
prefijo `APIN-*`, ticket `xygeni-product-backlog#1632`): `api-basic`, `api-basic-v2` (mismo
shape, endpoints distintos, para casos de "cambio de endpoints ya guardados"), `inventory-no-api`
(proyecto sin ningún framework de API, para las preconditions "sin APIs") y `spec-only` (solo un
`openapi.yaml`, sin código fuente).

### `inventory-assets/`

Fixtures del TP de **assets formales de inventario de APIs** (`test-plan-apisec-inventory-assets.html`,
prefijo `AIAS-*`, ticket `xygeni-product-backlog#1057`) — distinto de `inventory-synthesis/`
(#1632, reconciliación en el **servidor**): este TP prueba qué genera el propio **CLI/agente**
(modelo formal `api_service`/`api_module`/`api_endpoint`, ids deterministas, links `belongs_to`/
source-location) y el campo `issuesAssetMapping` del report de apisec, todo en local sin
`--upload`. `aias-api-basic` (Spring MVC, 2 módulos, 1 hallazgo PII deliberado, colisión
`GET /health` entre módulos) y `aias-no-api` (sin framework REST, caso negativo).

### `defect-verification/`

Fixtures de verificación de defectos concretos del backlog (ER de defect-verification, `APID-*`/`ASEC-*`),
un proyecto por ticket o escenario:
- `2102-g1-java-webxml` … `2102-g6-symfony-serviceid` — #2102, discovery en 6 formas de proyecto
  (Java web.xml, WebForms, MVC convencional, Go envuelto, scripts PHP, Symfony service id).
- `2110-go-middleware`, `2110-go-middleware-chi` — #2110, middleware de rutas Go como call expression.
- `2112-mass-assignment-drf` — #2112, `mass_assignment` en DRF.
- `2117-ssrf-drf` — #2117, `ssrf` con `url` de solo lectura en DRF.
- `2024-csharp-bola-imperative-auth` — #2024 (punto 3, fix #2018 / DepsDoctor#4450): BOLA en C# con la
  autenticación inferida desde `IAuthorizationService.AuthorizeAsync` imperativo (forma Orchard Core).
- `2024-go-exposure-pii` — #2024 (punto 1, parte Go): `excessive_data_exposure_go` / `pii_leak_in_response_go`.

Cada carpeta tiene su `README.md` con los casos esperados y, en los ya validados, los resultados reales.
