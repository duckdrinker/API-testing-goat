<?php

use App\Http\Controllers\ItemController;
use App\Http\Controllers\PingController;
use Illuminate\Support\Facades\Route;

Route::get('/items', [ItemController::class, 'index']);
Route::post('/items', [ItemController::class, 'store']);
Route::get('/ping', PingController::class);
