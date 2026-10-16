import { Body, Controller, Param, Post, Put } from '@nestjs/common';
import { WalletRepository } from './wallet.repository';

@Controller('wallets')
export class WalletController {
  constructor(private repo: WalletRepository) {}

  @Post()
  create(@Body() body: { amount: number }) {
    return this.repo.create(body);
  }

  @Put(':id')
  update(@Param('id') id: string, @Body() body: { amount: number }) {
    return this.repo.update(id, body);
  }
}
