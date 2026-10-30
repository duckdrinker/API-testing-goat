# 2309 Symfony (expectations written before scanning)
Registrations: config/routes.yaml (2-5, 7-10), config/config/routes/xml.xml (line 5; moved from config/routes_extra.xml after first scan found no XML route), attributes in AttrController.

| endpoint | expected handler file | expected line range | expected method/class | source |
|---|---|---|---|---|
| GET /yaml/list (YAML) | src/Controller/YamlController.php | 10-13 | list / YamlController | `public function list` |
| POST /yaml/create (YAML) | src/Controller/YamlController.php | 15-18 | create / YamlController | idem |
| GET /xml/show (XML) | src/Controller/XmlController.php | 10-13 | show / XmlController | idem |
| GET /attr/one (#[Route]) | src/Controller/AttrController.php | 12-15 (11-15 if attribute included) | one / AttrController | decorated method |
| POST /attr/two (#[Route]) | src/Controller/AttrController.php | 18-21 (17-21 if attribute included) | two / AttrController | idem |

Expected endpoint count: 5. registration populated for YAML/XML (other file); attribute case: same file as handler, registration optional.
