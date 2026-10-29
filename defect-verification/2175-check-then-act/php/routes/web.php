<?php

use App\Http\Controllers\CouponController;
use Illuminate\Support\Facades\Route;

Route::post('/coupons/{id}/redeem-unguarded', [CouponController::class, 'redeemUnguarded']);
Route::post('/coupons/{id}/redeem-split', [CouponController::class, 'redeemSplit']);
Route::post('/coupons/{id}/redeem-atomic', [CouponController::class, 'redeemAtomic']);
Route::post('/coupons/{id}/delete-after-check', [CouponController::class, 'deleteAfterCheck']);
Route::post('/coupons/{id}/already-done-write', [CouponController::class, 'alreadyDoneWrite']);
Route::post('/coupons/{code}/redeem-by-code', [CouponController::class, 'redeemByCode']);
