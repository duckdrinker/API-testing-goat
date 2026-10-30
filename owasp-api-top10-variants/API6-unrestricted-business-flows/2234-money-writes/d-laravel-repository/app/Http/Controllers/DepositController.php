<?php

namespace App\Http\Controllers;

use App\Repositories\DepositRepository;
use Illuminate\Http\Request;

class DepositController extends Controller
{
    public function __construct(protected DepositRepository $deposits)
    {
    }

    public function store(Request $request)
    {
        $this->deposits->create(['amount' => $request->input('amount')]);
        return response()->json(['ok' => true]);
    }

    public function transfer(Request $request)
    {
        $this->deposits->record($request->all());
        return response()->json(['ok' => true]);
    }
}
