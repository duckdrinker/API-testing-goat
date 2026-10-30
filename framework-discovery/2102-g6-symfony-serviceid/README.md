# Gap 6 - Symfony routes with DI service id
Expected: PUT /admin/ajax/product-taxons/positions resolved via services.yaml to ProductTaxonController::updateProductTaxonsPositionsAction.
Control: GET /admin/ajax/direct (class FQCN, already followable). Twin: /admin/ajax/unknown (service id not defined) -> no handler.
