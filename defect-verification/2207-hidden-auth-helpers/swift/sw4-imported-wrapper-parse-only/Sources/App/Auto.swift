import Vapor
import JWT

func autoLogin(_ req: Request) async throws -> HTTPStatus {
    let payload = try JwtUtil.decode(req)
    guard let id = UUID(payload.sub.value), let user = try await User.find(id, on: req.db) else { throw Abort(.notFound) }
    req.auth.login(user)
    return .ok
}
