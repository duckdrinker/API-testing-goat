'use strict';

async function getUser(ctx) {
  const id = ctx.params.id;
  ctx.body = { id };
}

module.exports = { getUser };
