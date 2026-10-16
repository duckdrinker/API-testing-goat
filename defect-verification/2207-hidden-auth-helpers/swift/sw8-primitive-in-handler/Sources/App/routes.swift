import Vapor

func routes(_ app: Application) throws {
    app.post("login") { req async throws -> HTTPStatus in
        let creds = try req.content.decode(Credentials.self)
        guard let user = try await User.query(on: req.db).filter(\.$email == creds.email).first(),
              try Bcrypt.verify(creds.password, created: user.passwordHash) else { throw Abort(.unauthorized) }
        req.auth.login(user)
        return .ok
    }
    app.post("auto-login", use: autoLogin)
}

struct Credentials: Content { var email: String; var password: String }
