<?php

namespace App\Http\Controllers;

use App\Models\User;
use Firebase\JWT\JWT;
use Firebase\JWT\Key;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class AutoController extends Controller
{
    private function subjectOf(string $token)
    {
        return $token;
    }

    public function autoLogin(Request $request)
    {
        $sub = $this->subjectOf($request->header('X-Token'));
        Auth::login(User::find($sub));
        return response()->json(['ok' => true]);
    }
}
