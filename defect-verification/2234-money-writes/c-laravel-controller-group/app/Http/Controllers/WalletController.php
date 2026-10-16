<?php

namespace App\Http\Controllers;

use App\Models\Deposit;
use Illuminate\Http\Request;

class WalletController extends Controller
{
    public function deposit(Request $request)
    {
        Deposit::create(['amount' => $request->input('amount')]);
        return response()->json(['ok' => true]);
    }

    public function balance(Request $request)
    {
        return response()->json(Deposit::sum('amount'));
    }
}
