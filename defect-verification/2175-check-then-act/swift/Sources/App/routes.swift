import Fluent
import Vapor

func routes(_ app: Application) throws {
    let coupons = app.grouped("coupons")

    // c1: unguarded check -> write
    coupons.post(":id", "redeem-unguarded") { req async throws -> HTTPStatus in
        let coupon = try await Coupon.find(req.parameters.get("id"), on: req.db)!
        if coupon.status == "open" {
            coupon.status = "used"
            try await coupon.save(on: req.db)
        }
        return .ok
    }

    // c2: check in the route closure, write in a service
    coupons.post(":id", "redeem-split") { req async throws -> HTTPStatus in
        let coupon = try await Coupon.find(req.parameters.get("id"), on: req.db)!
        if coupon.status == "open" {
            try await CouponService().markUsed(coupon, on: req.db)
        }
        return .ok
    }

    // c3: atomic conditional UPDATE
    coupons.post(":id", "redeem-atomic") { req async throws -> HTTPStatus in
        let coupon = try await Coupon.find(req.parameters.get("id"), on: req.db)!
        if coupon.status == "open" {
            try await Coupon.query(on: req.db)
                .filter(\.$id == coupon.id!)
                .filter(\.$status == "open")
                .set(\.$status, to: "used")
                .update()
        }
        return .ok
    }

    // c4: delete of the row just checked
    coupons.post(":id", "delete-after-check") { req async throws -> HTTPStatus in
        let coupon = try await Coupon.find(req.parameters.get("id"), on: req.db)!
        if coupon.status == "expired" {
            try await coupon.delete(on: req.db)
        }
        return .ok
    }

    // c6: already-done write
    coupons.post(":id", "already-done-write") { req async throws -> HTTPStatus in
        let coupon = try await Coupon.find(req.parameters.get("id"), on: req.db)!
        if coupon.status == "used" {
            coupon.status = "used"
            try await coupon.save(on: req.db)
        }
        return .ok
    }

    // c1v: query().filter().first() + status compare + update (variant of c1)
    coupons.post(":code", "redeem-by-code") { req async throws -> HTTPStatus in
        let code = req.parameters.get("code")!
        let coupon = try await Coupon.query(on: req.db).filter(\.$code == code).first()!
        if coupon.status == "open" {
            coupon.status = "used"
            try await coupon.update(on: req.db)
        }
        return .ok
    }
}
