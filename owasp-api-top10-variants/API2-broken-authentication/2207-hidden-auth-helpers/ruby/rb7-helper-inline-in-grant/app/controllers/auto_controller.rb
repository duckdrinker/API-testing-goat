class AutoController < ApplicationController
  def auto_login
    sign_in(User.find(verify_token(request.headers['X-Token'])['sub']))
    render json: { ok: true }
  end

  private

  def verify_token(token)
    JWT.decode(token, ENV['JWT_KEY'], true, algorithm: 'HS256').first
  end
end
