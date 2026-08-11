<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class FeedbackController extends Controller
{
    /**
     * ASEC-103 (PHP half of rate_limit_absence): authenticated, user-input
     * endpoint (see routes/api.php) with no `throttle` middleware or
     * RateLimiter::for(...) applied anywhere in this project.
     */
    public function store(Request $request)
    {
        return response()->json(['status' => 'created'], 201);
    }
}
