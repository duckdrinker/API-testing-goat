import {
  Controller,
  Get,
  Post,
  Put,
  Patch,
  Delete,
  All,
  Param,
  Query,
  Body,
  Headers,
  Session,
  Req,
  Res,
  Ip,
  HostParam,
  UploadedFile,
  HttpCode,
  UsePipes,
  ValidationPipe,
  UseGuards,
} from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth } from '@nestjs/swagger';
import { AuthGuard } from '@nestjs/passport';
import { RolesGuard } from './guards/roles.guard';
import { Roles } from './decorators/roles.decorator';
import { Public } from './decorators/public.decorator';

/**
 * Fixture controller for the NestJS API Discovery framework detector.
 *
 * See test-plan-apisec-framework-discovery.html.
 *
 * `@UseGuards` at the class level applies to every method below except
 * `healthCheck()`, which carries `@Public()` and therefore overrides it.
 */
@ApiTags('users')
@UseGuards(AuthGuard('jwt'), RolesGuard)
@Controller('users')
export class UsersController {

  // GET /users/:id
  // - @Param('id') with an explicit name -> PATH, named "id".
  // - @Query('expand') -> QUERY.
  @Get(':id')
  @ApiOperation({ summary: 'Get a user', description: 'Fetch a single user by id' })
  async getUser(@Param('id') id: string, @Query('expand') expand?: string) {
    return { id, expand: expand === 'true' };
  }

  // GET /users
  // - @Param() with no argument -> PATH, unnamed variant.
  @Get()
  @ApiOperation({ summary: 'List users' })
  async listUsers(@Param() filters: Record<string, string>) {
    return [];
  }

  // POST /users
  // - @Body() with no argument -> BODY, name "body".
  // - @HttpCode(201) -> tag status_code:201.
  // - @UsePipes(...) -> tag validated.
  @Post()
  @HttpCode(201)
  @UsePipes(new ValidationPipe())
  @ApiOperation({ summary: 'Create user', description: 'Creates a new user record' })
  async createUser(@Body() body: Record<string, unknown>) {
    return { created: true, ...body };
  }

  // PATCH /users/:id/email
  // - @Param('id') -> PATH, named.
  // - @Body('email') with an explicit argument -> BODY, name "email".
  @Patch(':id/email')
  @ApiOperation({ summary: 'Update the email of a user' })
  async updateEmail(@Param('id') id: string, @Body('email') email: string) {
    return { id, email };
  }

  // PUT /users/:id
  // - @Param('id') -> PATH, named.
  // - @Headers('x-request-id') -> HEADER.
  // - @ApiBearerAuth() -> metadata.
  @Put(':id')
  @ApiBearerAuth()
  @ApiOperation({ summary: 'Replace a user' })
  async replaceUser(@Param('id') id: string, @Headers('x-request-id') requestId: string) {
    return { id, requestId };
  }

  // DELETE /users/:id
  // - @Param('id') -> PATH, named.
  // - @Session() -> COOKIE.
  // - @Roles('ADMIN') is additive on top of the class-level @UseGuards.
  @Delete(':id')
  @Roles('ADMIN')
  @ApiOperation({ summary: 'Delete a user' })
  async deleteUser(@Param('id') id: string, @Session() session: Record<string, unknown>) {
    return { id, deleted: true };
  }

  // POST /users/:id/avatar
  // - @UploadedFile(), @Ip(), @HostParam() are all discarded parameters, never reported.
  @Post(':id/avatar')
  @ApiOperation({ summary: 'Upload a user avatar' })
  async uploadAvatar(
    @Param('id') id: string,
    @UploadedFile() file: unknown,
    @Ip() ip: string,
    @HostParam('tenant') tenant: string,
  ) {
    return { id, uploaded: true };
  }

  // ALL /users/healthz
  // - @All counts as GET for reporting.
  // - @Req()/@Res() are discarded parameters, never reported.
  // - @Public() overrides the class-level @UseGuards -> resulting security type "public".
  @All('healthz')
  @Public()
  async healthCheck(@Req() req: unknown, @Res() res: unknown) {
    return { status: 'ok' };
  }
}
