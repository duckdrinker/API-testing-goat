// plugin.js -- a Fastify plugin registered with a `prefix` option from app.js.
//
// KNOWN LIMITATION (see README.md): resolving fastify.register(plugin, { prefix })
// requires cross-file data flow (tracing the plugin function from its definition
// here back to the register() call site in app.js), which FastifyFrameworkDetector
// does not implement. The routes below are deliberately kept in this SEPARATE file
// -- rather than inlined into app.js -- specifically to exercise that cross-file gap.
module.exports = function v2Plugin(instance, opts, done) {
  instance.get('/status', (request, reply) => {
    reply.send({ status: 'ok' });
  });

  instance.get('/info', (request, reply) => {
    reply.send({ info: 'v2 plugin' });
  });

  done();
};
