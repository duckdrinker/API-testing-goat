<?php

namespace App\Http\Controllers;

use App\Models\User;
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class AutoController extends Controller
{
    private function parse(string $token)
    {
        return JWT::decode($token, new Key(env('JWT_KEY'), 'HS256'));
    }

    private function subjectFrom(string $token)
    {
        return $this->parse($token)->sub;
    }

    public function autoLogin(Request $request)
    {
        $sub = $this->subjectFrom($request->header('X-Token'));
        Auth::login(User::find($sub));
        return response()->json(['ok' => true]);
    }
}
