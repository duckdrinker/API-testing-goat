Rails.application.routes.draw do
  get '/articles', to: 'articles#index'
  post '/articles', to: 'articles#create'
  get '/articles/:id', to: 'articles#show'
  resources :widgets, only: [:index, :show]
end
