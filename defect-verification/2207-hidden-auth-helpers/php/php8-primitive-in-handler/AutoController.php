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
        $claims = JWT::decode($request->header('X-Token'), new Key(env('JWT_KEY'), 'HS256'));
        Auth::login(User::find($claims->sub));
        return response()->json(['ok' => true]);
    }
}
