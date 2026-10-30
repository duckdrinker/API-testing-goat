import Fluent
import Vapor

/// The application's own type for the authenticated caller.
final class AppUser: Authenticatable {
    var id: UUID?
    var username: String

    init(id: UUID? = nil, username: String) {
        self.id = id
        self.username = username
    }
}

final class Order: Model, Content {
    static let schema = "orders"

    @ID(key: .id) var id: UUID?
    @Field(key: "owner_id") var ownerId: UUID
    @Field(key: "status") var status: String

    init() {}
}
