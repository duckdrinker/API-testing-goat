<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Routing\Attribute\Route;

class AttrController extends AbstractController
{
    #[Route('/attr/one', name: 'attr_one', methods: ['GET'])]
    public function one(): JsonResponse
    {
        return new JsonResponse([]);
    }

    #[Route('/attr/two', name: 'attr_two', methods: ['POST'])]
    public function two(): JsonResponse
    {
        return new JsonResponse(['two' => true]);
    }
}
