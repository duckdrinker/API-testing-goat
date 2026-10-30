Rails.application.routes.draw do
  resources :wallets, only: [:create, :update]
  resources :accounts, only: [:update]
end
