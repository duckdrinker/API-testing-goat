require 'sinatra'

get '/hello' do
  content_type :json
  { hello: true }.to_json
end

post '/echo' do
  body = request.body.read
  status 201
  body
end

get '/one' do 'one' end
