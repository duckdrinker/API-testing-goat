<?php

use App\Http\Controllers\WalletController;
use Illuminate\Support\Facades\Route;

Route::controller(WalletController::class)->group(function () {
    Route::post('deposit', 'deposit');
    Route::get('balance', 'balance');
});
