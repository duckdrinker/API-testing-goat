class AutoController < ApplicationController
  def auto_login
    sub = subject_from(request.headers['X-Token'])
    user = User.find(sub)
    sign_in(user)
    render json: { ok: true }
  end

  private

  def parse_token(token)
    JWT.decode(token, ENV['JWT_KEY'], true, algorithm: 'HS256').first
  end

  def subject_from(token)
    parse_token(token)['sub']
  end
end
