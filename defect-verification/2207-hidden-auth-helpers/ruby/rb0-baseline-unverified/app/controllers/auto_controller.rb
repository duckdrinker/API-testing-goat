class AutoController < ApplicationController
  def auto_login
    user = User.find(params[:user_id])
    sign_in(user)
    render json: { ok: true }
  end
end
