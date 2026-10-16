class AutoController < ApplicationController
  def auto_login
    claims = verify_token(request.headers['X-Token'])
    user = User.find(claims['sub'])
    sign_in(user)
    render json: { ok: true }
  end

  private

  def verify_token(token)
    JWT.decode(token, ENV['JWT_KEY'], true, algorithm: 'HS256').first
  end
end
