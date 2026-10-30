<?php

namespace App\Http\Controllers;

use App\Models\User;
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class AutoController extends Controller
{
    public function autoLogin(Request $request)
    {
        Auth::login(User::find($request->input('user_id')));
        return response()->json(['ok' => true]);
    }
}
