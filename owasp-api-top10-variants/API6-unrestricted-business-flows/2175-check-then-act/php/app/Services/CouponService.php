<?php

namespace App\Services;

use App\Models\Coupon;

class CouponService
{
    public function markUsed(Coupon $coupon)
    {
        $coupon->status = 'used';
        $coupon->save();
    }
}
