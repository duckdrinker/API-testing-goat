<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;

class YamlController extends AbstractController
{
    public function list(): JsonResponse
    {
        return new JsonResponse([]);
    }

    public function create(): JsonResponse
    {
        return new JsonResponse(['ok' => true]);
    }
}
