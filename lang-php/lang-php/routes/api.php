<?php

use App\Http\Controllers\FeedbackController;
use App\Http\Controllers\UserController;
use Illuminate\Support\Facades\Route;

Route::middleware('auth:sanctum')->group(function () {
    Route::put('/users/{id}', [UserController::class, 'update']);
    Route::post('/feedback', [FeedbackController::class, 'store']);
});
