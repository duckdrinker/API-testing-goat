<?php

use App\Http\Controllers\OpaqueController;
use Illuminate\Support\Facades\Route;

Route::post('/opaque', [OpaqueController::class, 'opaque']);
Route::post('/money', [OpaqueController::class, 'money']);
Route::get('/read', [OpaqueController::class, 'read']);
Route::post('/note', [OpaqueController::class, 'note']);
