<?php

use Illuminate\Support\Facades\Route;

Route::post('/login', [App\Http\Controllers\LoginController::class, 'login']);
Route::post('/auto-login', [App\Http\Controllers\AutoController::class, 'autoLogin']);
