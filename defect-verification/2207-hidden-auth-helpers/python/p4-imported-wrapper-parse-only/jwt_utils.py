import jwt


def jwt_decode(token):
    return jwt.decode(token, options={"verify_signature": False})
