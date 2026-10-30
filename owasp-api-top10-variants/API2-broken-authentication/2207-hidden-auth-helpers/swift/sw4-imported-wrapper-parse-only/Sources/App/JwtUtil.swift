import Vapor
import JWT

enum JwtUtil {
    static func decode(_ req: Request) throws -> Payload {
        let token = (try? req.content.get(String.self, at: "token")) ?? ""
        return try JWTSigners().unverified(token, as: Payload.self)
    }
}
