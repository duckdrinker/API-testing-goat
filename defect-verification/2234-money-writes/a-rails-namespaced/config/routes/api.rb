namespace :api do
  namespace :v1 do
    resources :filters, only: [:index, :update]
  end
end
