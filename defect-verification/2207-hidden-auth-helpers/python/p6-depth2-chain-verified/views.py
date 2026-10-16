import jwt
from django.http import JsonResponse
from .models import User


def _decode(token, key):
    return jwt.decode(token, key, algorithms=["RS256"])


def _parse_id_token(token):
    return _decode(token, "jwks-public-key")


def promote(request):
    claims = _parse_id_token(request.POST["id_token"])
    user = User.objects.get(email=claims["email"])
    user.is_staff = True
    user.save()
    return JsonResponse({"ok": True})
