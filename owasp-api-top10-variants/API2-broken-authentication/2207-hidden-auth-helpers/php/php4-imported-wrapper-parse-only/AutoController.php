<?php

namespace App\Http\Controllers;

use App\Models\User;
use App\Support\JwtUtil;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

class AutoController extends Controller
{
    public function autoLogin(Request $request)
    {
        $claims = JwtUtil::decode($request->header('X-Token'));
        Auth::login(User::find($claims->sub));
        return response()->json(['ok' => true]);
    }
}
