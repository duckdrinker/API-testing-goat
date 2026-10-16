Rails.application.routes.draw do
  post '/login', to: 'sessions#create'
  post '/auto-login', to: 'auto#auto_login'
end
