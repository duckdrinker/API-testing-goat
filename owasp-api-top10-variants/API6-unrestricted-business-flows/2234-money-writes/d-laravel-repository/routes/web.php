<?php

use App\Http\Controllers\DepositController;
use Illuminate\Support\Facades\Route;

Route::post('/deposits', [DepositController::class, 'store']);
Route::post('/transfers', [DepositController::class, 'transfer']);
