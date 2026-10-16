require 'jwt'

module JwtUtil
  def self.decode(token)
    JWT.decode(token, ENV['JWT_KEY'], true, algorithm: 'HS256').first
  end
end
