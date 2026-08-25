<?php

namespace App\Http\Controllers;

use App\Models\Account;
use Illuminate\Http\Request;

/**
 * xygeni-product-backlog#1552: Account::create($request->all()) with
 * $guarded = [] on the model — every column, not just a documented
 * $fillable list, is mass-assignable.
 */
class AccountController extends Controller
{
    public function store(Request $request)
    {
        $account = Account::create($request->all());

        return response()->json($account);
    }
}
