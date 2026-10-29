<?php

namespace App\Http\Controllers;

class ItemController extends Controller
{
    public function index()
    {
        return response()->json([]);
    }

    public function store()
    {
        return response()->json(['ok' => true]);
    }
}
