import jwt
from django.conf import settings
from django.http import JsonResponse
from .models import User


def _verified_role(token):
    return jwt.decode(token, settings.SECRET_KEY, algorithms=["HS256"])["role"]


def promote(request):
    user = User.objects.get(pk=request.POST["user_id"])
    # the verifying helper is called inside the very expression that grants the role
    user.role = _verified_role(request.POST["token"])
    user.save()
    return JsonResponse({"ok": True})
