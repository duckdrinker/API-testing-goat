<?php

namespace App\Http\Controllers;

use App\Models\User;
use Illuminate\Http\Request;

class UserController extends Controller
{
    /**
     * ASEC-097 (mass_assignment): the whole request body is mass-assigned
     * onto the model via fill(), and "role" is not excluded from
     * User::$fillable - a client can set it directly.
     */
    public function update(Request $request, $id)
    {
        $user = User::findOrFail($id);
        $user->fill($request->all());
        $user->save();

        return response()->json($user);
    }
}
