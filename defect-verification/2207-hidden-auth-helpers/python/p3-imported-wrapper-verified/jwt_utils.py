import jwt
from django.conf import settings


def jwt_decode(token):
    return jwt.decode(token, settings.SECRET_KEY, algorithms=["HS256"])
