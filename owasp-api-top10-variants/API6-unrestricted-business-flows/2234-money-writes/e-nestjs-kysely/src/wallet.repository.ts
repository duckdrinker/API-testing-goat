import { Injectable } from '@nestjs/common';
import { Kysely } from 'kysely';

@Injectable()
export class WalletRepository {
  constructor(private db: Kysely<any>) {}

  create(body: { amount: number }) {
    return this.db.insertInto('wallet').values({ amount: body.amount }).execute();
  }

  update(id: string, body: { amount: number }) {
    return this.db.updateTable('wallet').set({ balance: body.amount }).where('id', '=', id).execute();
  }
}
