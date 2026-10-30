import Vapor

struct TodoController {
    func index(req: Request) async throws -> [String] {
        return []
    }

    func create(req: Request) async throws -> String {
        let x = "created"
        return x
    }
}
