<?php

namespace App\Http\Controllers;

use App\Models\Sale;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;

/**
 * #2214 - owner bound through a local variable (Laravel / Eloquent).
 * NOT REPORTED cases must stay silent; REPORTED cases must fire
 * php.user_controlled_primary_key.
 */
class SaleController extends Controller
{
    // CTRL-1 REPORTED: unbounded lookup, baseline positive.
    public function ctrl1(Request $request)
    {
        return Sale::findOrFail($request->input('sale_id'));
    }

    // CTRL-2 NOT REPORTED: caller written inline in the bound.
    public function ctrl2(Request $request)
    {
        return Sale::where('id', $request->input('sale_id'))->where('owner', Auth::id())->firstOrFail();
    }

    // A1 NOT REPORTED: bound in the lookup, caller held in a local.
    public function a1(Request $request)
    {
        $owner = Auth::id();
        return Sale::where('id', $request->input('sale_id'))->where('owner', $owner)->firstOrFail();
    }

    // A2 REPORTED: twin of A1, the local holds a request value.
    public function a2(Request $request)
    {
        $owner = $request->input('owner');
        return Sale::where('id', $request->input('sale_id'))->where('owner', $owner)->firstOrFail();
    }

    // B1 NOT REPORTED: comparison after the lookup, caller held in a local.
    public function b1(Request $request)
    {
        $owner = Auth::id();
        $sale = Sale::findOrFail($request->input('sale_id'));
        if ($sale->owner !== $owner) {
            abort(403);
        }
        return $sale;
    }

    // B2 REPORTED: twin of B1, the local holds a request value.
    public function b2(Request $request)
    {
        $owner = $request->input('owner');
        $sale = Sale::findOrFail($request->input('sale_id'));
        if ($sale->owner !== $owner) {
            abort(403);
        }
        return $sale;
    }

    // C1 REPORTED: local first takes the caller, then is rebound to a request value.
    public function c1(Request $request)
    {
        $owner = Auth::id();
        $owner = $request->input('owner');
        return Sale::where('id', $request->input('sale_id'))->where('owner', $owner)->firstOrFail();
    }

    // C2 REPORTED: rebound local, comparison after the lookup.
    public function c2(Request $request)
    {
        $owner = Auth::id();
        $owner = $request->input('owner');
        $sale = Sale::findOrFail($request->input('sale_id'));
        if ($sale->owner !== $owner) {
            abort(403);
        }
        return $sale;
    }

    // D1 REPORTED: loop variable, not assigned once from the caller.
    public function d1(Request $request)
    {
        $found = null;
        foreach ($request->input('owners', []) as $owner) {
            $found = Sale::where('id', $request->input('sale_id'))->where('owner', $owner)->first();
        }
        return $found;
    }

    // W1 NOT REPORTED: `where` on the caller earlier in the chain, then findOrFail.
    public function w1(Request $request)
    {
        return Sale::where('owner', Auth::id())->findOrFail($request->input('sale_id'));
    }

    // W2 REPORTED: twin of W1, the `where` uses a request value.
    public function w2(Request $request)
    {
        return Sale::where('owner', $request->input('owner'))->findOrFail($request->input('sale_id'));
    }

    // W3 REPORTED: `orWhere` on the caller is not a bound (it widens the query).
    public function w3(Request $request)
    {
        return Sale::orWhere('owner', Auth::id())->findOrFail($request->input('sale_id'));
    }

    // W4 NOT REPORTED: `where` earlier in the chain with the caller held in a local.
    public function w4(Request $request)
    {
        $owner = Auth::id();
        return Sale::where('owner', $owner)->findOrFail($request->input('sale_id'));
    }
}
