<?php

namespace App\Support;

use Firebase\JWT\JWT;
use Firebase\JWT\Key;

class JwtUtil
{
    public static function decode(string $token)
    {
        return JWT::decode($token, new Key(env('JWT_KEY'), 'HS256'));
    }
}
