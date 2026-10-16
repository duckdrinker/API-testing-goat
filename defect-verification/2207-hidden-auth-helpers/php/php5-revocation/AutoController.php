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
        Auth::logout();
        $request->session()->invalidate();
        return response()->json(['ok' => true]);
    }
}
