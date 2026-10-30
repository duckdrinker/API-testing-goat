import Fluent
import Vapor

// #2214 - owner bound through a local variable (Vapor + Fluent).
// NOT REPORTED cases must stay silent; REPORTED cases must fire
// swift.user_controlled_primary_key.

func routes(_ app: Application) throws {

    // CTRL-1 REPORTED: unbounded lookup, baseline positive.
    app.get("ctrl1", ":id") { req async throws -> Order in
        guard let order = try await Order.find(req.parameters.get("id"), on: req.db) else {
            throw Abort(.notFound)
        }
        return order
    }

    // CTRL-2 NOT REPORTED: comparison after the lookup, caller written inline.
    app.get("ctrl2", ":id") { req async throws -> Order in
        guard let order = try await Order.find(req.parameters.get("id"), on: req.db) else {
            throw Abort(.notFound)
        }
        if try order.ownerId != req.auth.require(AppUser.self).id { throw Abort(.forbidden) }
        return order
    }

    // A1 NOT REPORTED: bound in the lookup, caller held in a local.
    app.get("a1", ":id") { req async throws -> Order in
        let ownerId = try req.auth.require(AppUser.self).id!
        let id = req.parameters.get("id", as: UUID.self)
        guard let order = try await Order.query(on: req.db)
            .filter(\.$id == id)
            .filter(\.$ownerId == ownerId)
            .first() else { throw Abort(.notFound) }
        return order
    }

    // A2 REPORTED: twin of A1, the local holds a request value.
    app.get("a2", ":id") { req async throws -> Order in
        let ownerId = try req.query.get(UUID.self, at: "owner")
        let id = req.parameters.get("id", as: UUID.self)
        guard let order = try await Order.query(on: req.db)
            .filter(\.$id == id)
            .filter(\.$ownerId == ownerId)
            .first() else { throw Abort(.notFound) }
        return order
    }

    // B1 NOT REPORTED: comparison after the lookup, local holds the principal object.
    app.get("b1", ":id") { req async throws -> Order in
        let caller = try req.auth.require(AppUser.self)
        guard let order = try await Order.find(req.parameters.get("id"), on: req.db) else {
            throw Abort(.notFound)
        }
        if order.ownerId != caller.id { throw Abort(.forbidden) }
        return order
    }

    // B1f NOT REPORTED: comparison after the lookup, local holds one of its fields.
    app.get("b1f", ":id") { req async throws -> Order in
        let caller = try req.auth.require(AppUser.self)
        let ownerId = caller.id
        guard let order = try await Order.find(req.parameters.get("id"), on: req.db) else {
            throw Abort(.notFound)
        }
        if order.ownerId != ownerId { throw Abort(.forbidden) }
        return order
    }

    // B2 REPORTED: twin of B1f, the local holds a request value.
    app.get("b2", ":id") { req async throws -> Order in
        let ownerId = try req.query.get(UUID.self, at: "owner")
        guard let order = try await Order.find(req.parameters.get("id"), on: req.db) else {
            throw Abort(.notFound)
        }
        if order.ownerId != ownerId { throw Abort(.forbidden) }
        return order
    }

    // C1 REPORTED: local first takes the caller, then is rebound to a request value.
    app.get("c1", ":id") { req async throws -> Order in
        var ownerId = try req.auth.require(AppUser.self).id!
        ownerId = try req.query.get(UUID.self, at: "owner")
        let id = req.parameters.get("id", as: UUID.self)
        guard let order = try await Order.query(on: req.db)
            .filter(\.$id == id)
            .filter(\.$ownerId == ownerId)
            .first() else { throw Abort(.notFound) }
        return order
    }

    // C2 REPORTED: rebound local, comparison after the lookup.
    app.get("c2", ":id") { req async throws -> Order in
        var ownerId = try req.auth.require(AppUser.self).id!
        ownerId = try req.query.get(UUID.self, at: "owner")
        guard let order = try await Order.find(req.parameters.get("id"), on: req.db) else {
            throw Abort(.notFound)
        }
        if order.ownerId != ownerId { throw Abort(.forbidden) }
        return order
    }

    // D1 REPORTED: loop variable, not assigned once from the caller.
    app.get("d1", ":id") { req async throws -> Order in
        let id = req.parameters.get("id", as: UUID.self)
        var found: Order?
        for ownerId in try req.query.get([UUID].self, at: "owners") {
            found = try await Order.query(on: req.db)
                .filter(\.$id == id)
                .filter(\.$ownerId == ownerId)
                .first()
        }
        guard let order = found else { throw Abort(.notFound) }
        return order
    }
}
