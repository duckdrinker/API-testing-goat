Rails.application.routes.draw do
  post '/coupons/:id/redeem_unguarded', to: 'coupons#redeem_unguarded'
  post '/coupons/:id/redeem_split', to: 'coupons#redeem_split'
  post '/coupons/:id/redeem_helper', to: 'coupons#redeem_helper'
  post '/coupons/:id/redeem_atomic', to: 'coupons#redeem_atomic'
  post '/coupons/:id/delete_after_check', to: 'coupons#delete_after_check'
  post '/coupons/:id/already_done_write', to: 'coupons#already_done_write'
  post '/coupons/:code/redeem_by_code', to: 'coupons#redeem_by_code'
end
