package demo;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {
    public Mono<ServerResponse> listUsers(ServerRequest request) {
        return ServerResponse.ok().build();
    }
}
