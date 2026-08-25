// lang-fastify fixture -- API Discovery framework detection (FastifyFrameworkDetector).
// Static-analysis fixture, not meant to be run.

const Fastify = require('fastify');
const fastify = Fastify({ logger: true });
const v2Plugin = require('./plugin');

function requireAuth(request, reply, done) {
  if (!request.headers.authorization) {
    reply.code(401).send();
    return;
  }
  done();
}

function requireRole(role) {
  return (request, reply, done) => {
    if (request.headers['x-user-role'] !== role) {
      reply.code(403).send();
      return;
    }
    done();
  };
}

// Shorthand form.
fastify.get('/api/items/:id', (request, reply) => {
  reply.send({ id: request.params.id });
});

fastify.post('/api/items', (request, reply) => {
  reply.send({ created: true });
});

// Full route() form, `preHandler` as a single function.
fastify.route({
  method: 'GET',
  url: '/api/private',
  preHandler: requireAuth,
  handler: (request, reply) => {
    reply.send({ secret: true });
  },
});

// Full route() form, `preHandler` as an array -- both entries are inspected as
// auth middleware, same as the single-value form above.
fastify.route({
  method: 'POST',
  url: '/api/admin',
  preHandler: [requireAuth, requireRole('admin')],
  handler: (request, reply) => {
    reply.code(204).send();
  },
});

// Catch-all verb.
fastify.all('/api/webhook', (request, reply) => {
  reply.send({ received: true });
});

// KNOWN LIMITATION: fastify.register(plugin, { prefix }) is not resolved across
// files. v2Plugin (see plugin.js) declares GET /status and GET /info; neither is
// expected to appear in the discovery output, with or without the /v2 prefix.
// See README.md.
fastify.register(v2Plugin, { prefix: '/v2' });

fastify.listen({ port: 3000 }, () => console.log('lang-fastify fixture listening on port 3000'));
