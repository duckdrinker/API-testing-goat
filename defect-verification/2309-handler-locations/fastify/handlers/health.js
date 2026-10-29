'use strict';

async function health(request, reply) {
  return { status: 'ok' };
}

module.exports = { health };
