<?php

namespace App\Http\Controllers;

use App\Models\Coupon;
use App\Services\CouponService;
use Illuminate\Support\Facades\DB;

class CouponController extends Controller
{
    public function __construct(protected CouponService $service)
    {
    }

    // c1: unguarded check -> write
    public function redeemUnguarded($id)
    {
        $coupon = Coupon::find($id);
        if ($coupon->status === 'open') {
            $coupon->status = 'used';
            $coupon->save();
        }
        return response()->json(['ok' => true]);
    }

    // c2: check in controller, write in service
    public function redeemSplit($id)
    {
        $coupon = Coupon::find($id);
        if ($coupon->status === 'open') {
            $this->service->markUsed($coupon);
        }
        return response()->json(['ok' => true]);
    }

    // c3: atomic conditional UPDATE
    public function redeemAtomic($id)
    {
        $coupon = Coupon::find($id);
        if ($coupon->status === 'open') {
            DB::update("UPDATE coupons SET status = 'used' WHERE id = ? AND status = 'open'", [$id]);
        }
        return response()->json(['ok' => true]);
    }

    // c4: delete of the row just checked
    public function deleteAfterCheck($id)
    {
        $coupon = Coupon::find($id);
        if ($coupon->status === 'expired') {
            $coupon->delete();
        }
        return response()->json(['ok' => true]);
    }

    // c6: already-done write
    public function alreadyDoneWrite($id)
    {
        $coupon = Coupon::find($id);
        if ($coupon->status === 'used') {
            $coupon->status = 'used';
            $coupon->save();
        }
        return response()->json(['ok' => true]);
    }

    // c1v: where()->first() + status compare + update (variant of c1)
    public function redeemByCode($code)
    {
        $coupon = Coupon::where('code', $code)->first();
        if ($coupon->status === 'open') {
            $coupon->update(['status' => 'used']);
        }
        return response()->json(['ok' => true]);
    }
}
