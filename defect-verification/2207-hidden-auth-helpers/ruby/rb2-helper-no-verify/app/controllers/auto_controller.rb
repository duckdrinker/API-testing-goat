class AutoController < ApplicationController
  def auto_login
    sub = subject_of(request.headers['X-Token'])
    user = User.find(sub)
    sign_in(user)
    render json: { ok: true }
  end

  private

  def subject_of(token)
    token
  end
end
