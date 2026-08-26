<?php

namespace App\Models;

use Illuminate\Foundation\Auth\User as Authenticatable;

class User extends Authenticatable
{
    // ASEC-097: "role" is fillable, so a client-supplied value binds
    // straight through on mass assignment.
    protected $fillable = ['name', 'email', 'role'];
}
