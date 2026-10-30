import Vapor
import JWT

enum JwtUtil {
    static func decode(_ req: Request) throws -> Payload {
        try req.jwt.verify(as: Payload.self)
    }
}
