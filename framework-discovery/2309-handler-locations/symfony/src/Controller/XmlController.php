<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;

class XmlController extends AbstractController
{
    public function show(): JsonResponse
    {
        return new JsonResponse([]);
    }
}
