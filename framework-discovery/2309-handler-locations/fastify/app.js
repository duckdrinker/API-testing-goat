'use strict';
const fastify = require('fastify')();
const { health } = require('./handlers/health');

fastify.get('/health', health);

fastify.post('/inline', async (request, reply) => {
  const body = request.body;
  reply.code(201);
  return {
    created: body,
  };
});

fastify.listen({ port: 3000 });
