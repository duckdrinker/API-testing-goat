import Vapor
import JWT

func parsePayload(_ req: Request) throws -> Payload {
    try req.jwt.verify(as: Payload.self)
}

func subjectFrom(_ req: Request) throws -> String {
    try parsePayload(req).sub.value
}

func autoLogin(_ req: Request) async throws -> HTTPStatus {
    let sub = try subjectFrom(req)
    guard let id = UUID(sub), let user = try await User.find(id, on: req.db) else { throw Abort(.notFound) }
    req.auth.login(user)
    return .ok
}
