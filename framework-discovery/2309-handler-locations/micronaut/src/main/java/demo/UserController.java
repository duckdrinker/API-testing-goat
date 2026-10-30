package demo;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

@Controller("/users")
public class UserController {
    @Get
    public String list() {
        return "[]";
    }

    @Post("/new")
    public String create() {
        return "ok";
    }
}
