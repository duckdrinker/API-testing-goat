class CouponsController < ApplicationController
  # c1: unguarded check -> write
  def redeem_unguarded
    coupon = Coupon.find(params[:id])
    if coupon.status == 'open'
      coupon.update!(status: 'used')
    end
    render json: { ok: true }
  end

  # c2: check in controller, write in a service
  def redeem_split
    coupon = Coupon.find(params[:id])
    if coupon.status == 'open'
      CouponService.new.mark_used(coupon)
    end
    render json: { ok: true }
  end

  # c2b: check in the action, write in a private helper of the same controller
  def redeem_helper
    coupon = Coupon.find(params[:id])
    if coupon.status == 'open'
      mark_used(coupon)
    end
    render json: { ok: true }
  end

  # c3: atomic conditional UPDATE
  def redeem_atomic
    coupon = Coupon.find(params[:id])
    if coupon.status == 'open'
      Coupon.connection.execute("UPDATE coupons SET status = 'used' WHERE id = #{coupon.id.to_i} AND status = 'open'")
    end
    render json: { ok: true }
  end

  # c4: delete of the row just checked
  def delete_after_check
    coupon = Coupon.find(params[:id])
    if coupon.status == 'expired'
      coupon.destroy
    end
    render json: { ok: true }
  end

  # c6: already-done write
  def already_done_write
    coupon = Coupon.find(params[:id])
    if coupon.status == 'used'
      coupon.update!(status: 'used')
    end
    render json: { ok: true }
  end

  # c1v: find_by + status compare + save (variant of c1)
  def redeem_by_code
    coupon = Coupon.find_by(code: params[:code])
    if coupon.status == 'open'
      coupon.status = 'used'
      coupon.save
    end
    render json: { ok: true }
  end

  private

  def mark_used(coupon)
    coupon.update!(status: 'used')
  end
end
