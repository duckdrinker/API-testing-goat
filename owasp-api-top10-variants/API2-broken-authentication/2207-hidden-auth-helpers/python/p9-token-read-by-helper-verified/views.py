import jwt
from django.conf import settings
from django.http import JsonResponse
from .models import User


def read_token(request):
    return request.POST["token"]


class RefreshToken:
    @classmethod
    def promote(cls, request):
        token = read_token(request)
        payload = jwt.decode(token, settings.SECRET_KEY, algorithms=["HS256"])
        user = User.objects.get(pk=payload["sub"])
        user.is_staff = True
        user.save()
        return JsonResponse({"ok": True})
