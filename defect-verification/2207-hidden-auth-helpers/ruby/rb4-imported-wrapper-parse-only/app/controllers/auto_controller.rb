class AutoController < ApplicationController
  def auto_login
    claims = JwtUtil.decode(request.headers['X-Token'])
    user = User.find(claims['sub'])
    sign_in(user)
    render json: { ok: true }
  end
end
