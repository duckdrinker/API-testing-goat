<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * xygeni-product-backlog#1552 (mass_assignable_model, low): $guarded only
 * names keys/timestamps — every business attribute (amount, status,
 * ownerId, ...) is still mass-assignable.
 */
class Invoice extends Model
{
    protected $guarded = ['id', 'created_at', 'updated_at'];
}
