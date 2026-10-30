<?php

namespace App\Http\Controllers;

use App\Models\Deposit;
use Illuminate\Http\Request;

class OpaqueController extends Controller
{
    // Opaque write: counts as a write, NOT as a money write
    public function opaque(Request $request)
    {
        Deposit::create($request->all());
        return response()->json(['ok' => true]);
    }

    // Money write
    public function money(Request $request)
    {
        Deposit::create(['amount' => $request->input('amount')]);
        return response()->json(['ok' => true]);
    }

    // Read only
    public function read(Request $request)
    {
        return response()->json(Deposit::all());
    }

    // Opaque write on an entity with no money field: a write, never a money write
    public function note(Request $request)
    {
        \App\Models\Note::create($request->all());
        return response()->json(['ok' => true]);
    }
}
