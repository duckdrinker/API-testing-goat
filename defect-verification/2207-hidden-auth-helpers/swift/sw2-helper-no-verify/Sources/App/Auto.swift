import Vapor
import JWT

func subjectOf(_ req: Request) -> String {
    (try? req.content.get(String.self, at: "token")) ?? ""
}

func autoLogin(_ req: Request) async throws -> HTTPStatus {
    guard let id = UUID(subjectOf(req)), let user = try await User.find(id, on: req.db) else { throw Abort(.notFound) }
    req.auth.login(user)
    return .ok
}
