<?php

namespace App\Services;

use Firebase\JWT\JWT;

class JwtService
{
    /**
     * ASEC-090 (jwt_misconfiguration): the allowed-algorithms list passed
     * to decode() includes "none", so an unsigned token is accepted as valid.
     */
    public function verify(string $token, string $key)
    {
        return JWT::decode($token, $key, ['HS256', 'none']);
    }
}
