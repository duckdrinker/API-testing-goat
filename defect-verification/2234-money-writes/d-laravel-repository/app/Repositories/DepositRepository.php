<?php

namespace App\Repositories;

use App\Models\Deposit;
use Prettus\Repository\Eloquent\BaseRepository;

class DepositRepository extends BaseRepository
{
    public function model()
    {
        return Deposit::class;
    }

    public function record(array $data)
    {
        return $this->model->create($data);
    }
}
