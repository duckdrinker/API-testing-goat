class AutoController < ApplicationController
  def auto_login
    claims = JWT.decode(request.headers['X-Token'], ENV['JWT_KEY'], true, algorithm: 'HS256').first
    user = User.find(claims['sub'])
    sign_in(user)
    render json: { ok: true }
  end
end
