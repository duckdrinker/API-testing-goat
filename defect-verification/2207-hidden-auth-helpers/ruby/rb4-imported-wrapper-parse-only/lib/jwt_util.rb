require 'jwt'

module JwtUtil
  def self.decode(token)
    JWT.decode(token, nil, false).first
  end
end
