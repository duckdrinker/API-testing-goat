import jwt
from django.conf import settings
from django.http import JsonResponse
from .models import User


class RefreshToken:
    @classmethod
    def clean_token(cls, token):
        return jwt.decode(token, settings.SECRET_KEY, algorithms=["HS256"])

    @classmethod
    def promote(cls, request):
        payload = cls.clean_token(request.POST["token"])
        user = User.objects.get(pk=payload["sub"])
        user.is_staff = True
        user.save()
        return JsonResponse({"ok": True})
