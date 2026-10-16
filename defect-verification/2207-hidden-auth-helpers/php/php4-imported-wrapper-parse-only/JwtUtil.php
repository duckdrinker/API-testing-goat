<?php

namespace App\Support;

class JwtUtil
{
    public static function decode(string $token)
    {
        $parts = explode('.', $token);
        return json_decode(base64_decode($parts[1]));
    }
}
