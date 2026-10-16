import Vapor
import JWT

func verifiedSubject(_ req: Request) throws -> String {
    try req.jwt.verify(as: Payload.self).sub.value
}

func autoLogin(_ req: Request) async throws -> HTTPStatus {
    req.auth.login(try await User.find(UUID(verifiedSubject(req)), on: req.db)!)
    return .ok
}
