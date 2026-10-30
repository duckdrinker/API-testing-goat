import Vapor
import JWT

func readToken(_ req: Request) -> String {
    (try? req.content.get(String.self, at: "token")) ?? ""
}

func autoLogin(_ req: Request) async throws -> HTTPStatus {
    let token = readToken(req)
    let payload = try req.jwt.verify(token, as: Payload.self)
    guard let id = UUID(payload.sub.value), let user = try await User.find(id, on: req.db) else { throw Abort(.notFound) }
    req.auth.login(user)
    return .ok
}
