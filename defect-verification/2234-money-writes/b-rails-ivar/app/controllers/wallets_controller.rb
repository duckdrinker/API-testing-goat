class WalletsController < ApplicationController
  def create
    @wallet = Wallet.new(balance: params[:amount])
    @wallet.save
    render json: @wallet
  end

  def update
    @wallet = Wallet.find(params[:id])
    @wallet.update!(balance: params[:amount])
    render json: @wallet
  end
end
