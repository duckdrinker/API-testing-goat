import Vapor
import JWT

func autoLogin(_ req: Request) async throws -> HTTPStatus {
    req.auth.logout(User.self)
    req.session.destroy()
    return .ok
}
