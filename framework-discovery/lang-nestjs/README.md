# Language coverage — NestJS

**Xygeni component:** API Discovery framework detector `nestjs`

`package.json` declares `@nestjs/core` (maps to framework `nestjs` for autodiscovery).

## What this fixture demonstrates

- `UsersController` class — `@Controller('users')` with `@UseGuards(AuthGuard('jwt'), RolesGuard)`
  (both guards extracted); `@ApiTags('users')` metadata → framework discovery.
- `getUser()` (`GET /users/:id`) — `@Param('id')` with an explicit name → PATH, named `id`;
  `@Query('expand')` → QUERY; `@ApiOperation({summary, description})` → framework discovery.
- `listUsers()` (`GET /users`) — `@Param()` with no argument → PATH, unnamed variant → framework
  discovery.
- `createUser()` (`POST /users`) — `@Body()` with no argument → BODY, name `"body"`;
  `@HttpCode(201)` → tag `status_code:201`; `@UsePipes(new ValidationPipe())` → tag `validated` →
  framework discovery.
- `updateEmail()` (`PATCH /users/:id/email`) — `@Param('id')` → PATH; `@Body('email')` with an
  explicit argument → BODY, name `email` → framework discovery.
- `replaceUser()` (`PUT /users/:id`) — `@Headers('x-request-id')` → HEADER; `@ApiBearerAuth()` →
  framework discovery.
- `deleteUser()` (`DELETE /users/:id`) — `@Session()` → COOKIE; `@Roles('ADMIN')` is additive on
  top of the class-level `@UseGuards` → framework discovery.
- `uploadAvatar()` (`POST /users/:id/avatar`) — `@UploadedFile()`, `@Ip()`, `@HostParam('tenant')`
  are all discarded parameters, never reported → framework discovery.
- `healthCheck()` (`ALL /users/healthz`) — `@All` counts as GET for reporting; `@Req()`/`@Res()`
  are discarded parameters, never reported; `@Public()` overrides the class-level `@UseGuards`,
  so the resulting security type is `"public"` → framework discovery.

## Expected scan result

```
xygeni apisecurity --dir . --discovery-only -f json
```
