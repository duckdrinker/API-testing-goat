import Vapor

func routes(_ app: Application) throws {
    let ctrl = TodoController()
    app.get("todos", use: ctrl.index)
    app.post("todos", use: ctrl.create)
    app.get("closure") { req async -> String in
        let a = "closure"
        return a
    }
}
