class CouponService
  def mark_used(coupon)
    coupon.status = 'used'
    coupon.save!
  end
end
