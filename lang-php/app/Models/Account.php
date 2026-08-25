<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

/**
 * xygeni-product-backlog#1552 (mass_assignable_model, high): guarded = []
 * leaves every column mass-assignable — strictly more dangerous than an
 * over-broad $fillable, since there is no field list at all to enumerate.
 */
class Account extends Model
{
    protected $guarded = [];
}
