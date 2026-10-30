<?php

namespace App\Http\Controllers;

use App\Models\User;
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class AutoController extends Controller
{
    private function verifyToken(string $token)
    {
        return JWT::decode($token, new Key(env('JWT_KEY'), 'HS256'));
    }

    public function autoLogin(Request $request)
    {
        Auth::login(User::find($this->verifyToken($request->header('X-Token'))->sub));
        return response()->json(['ok' => true]);
    }
}
