class AccountsController < ApplicationController
  before_action :set_account, only: [:update]

  def update
    @account.update!(balance: params[:amount])
    render json: @account
  end

  private

  def set_account
    @account = Account.find(params[:id])
  end
end
