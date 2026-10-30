import Vapor
import JWT

func autoLogin(_ req: Request) async throws -> HTTPStatus {
    let token = try req.content.get(String.self, at: "token")
    guard let id = UUID(token), let user = try await User.find(id, on: req.db) else { throw Abort(.notFound) }
    req.auth.login(user)
    return .ok
}
