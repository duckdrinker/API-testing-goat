<?php

use App\Http\Controllers\SaleController;
use Illuminate\Support\Facades\Route;

foreach (['ctrl1', 'ctrl2', 'a1', 'a2', 'b1', 'b2', 'c1', 'c2', 'd1', 'w1', 'w2', 'w3', 'w4'] as $case) {
    Route::get("/sales/$case", [SaleController::class, $case]);
}
