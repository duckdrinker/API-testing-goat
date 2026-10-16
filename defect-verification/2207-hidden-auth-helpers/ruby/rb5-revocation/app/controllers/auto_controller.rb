class AutoController < ApplicationController
  def auto_login
    sign_out(current_user)
    render json: { ok: true }
  end
end
