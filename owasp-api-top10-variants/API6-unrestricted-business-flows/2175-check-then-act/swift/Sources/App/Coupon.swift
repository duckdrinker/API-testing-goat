import Fluent
import Vapor

final class Coupon: Model, Content {
    static let schema = "coupons"

    @ID(key: .id) var id: UUID?
    @Field(key: "code") var code: String
    @Field(key: "status") var status: String

    init() {}
}

struct CouponService {
    func markUsed(_ coupon: Coupon, on db: Database) async throws {
        coupon.status = "used"
        try await coupon.save(on: db)
    }
}
