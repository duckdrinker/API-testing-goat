<?php
namespace App\Controller;

use Symfony\Component\HttpFoundation\JsonResponse;

class DirectController
{
    public function indexAction(): JsonResponse { return new JsonResponse([]); }
}
