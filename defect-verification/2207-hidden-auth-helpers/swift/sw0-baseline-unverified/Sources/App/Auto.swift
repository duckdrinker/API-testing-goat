import Vapor
import JWT

func autoLogin(_ req: Request) async throws -> HTTPStatus {
    let id = try req.content.get(UUID.self, at: "userId")
    guard let user = try await User.find(id, on: req.db) else { throw Abort(.notFound) }
    req.auth.login(user)
    return .ok
}
