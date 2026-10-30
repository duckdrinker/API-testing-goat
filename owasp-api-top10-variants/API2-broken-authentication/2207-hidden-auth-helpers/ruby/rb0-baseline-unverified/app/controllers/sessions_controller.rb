class SessionsController < ApplicationController
  def create
    user = User.find_by(email: params[:email])
    if user&.valid_password?(params[:password])
      sign_in(user)
      render json: { ok: true }
    else
      render json: { ok: false }, status: :unauthorized
    end
  end
end
