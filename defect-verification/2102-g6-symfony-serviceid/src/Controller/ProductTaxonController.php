<?php
namespace App\Controller;

use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\JsonResponse;

class ProductTaxonController
{
    public function updateProductTaxonsPositionsAction(Request $request): JsonResponse
    {
        return new JsonResponse(['ok' => $request->request->all()]);
    }
}
