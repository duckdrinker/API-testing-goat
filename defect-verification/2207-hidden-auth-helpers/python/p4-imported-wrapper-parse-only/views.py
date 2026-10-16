from django.http import JsonResponse
from .jwt_utils import jwt_decode
from .models import User


def promote(request):
    payload = jwt_decode(request.POST["token"])
    user = User.objects.get(pk=payload["sub"])
    user.is_staff = True
    user.save()
    return JsonResponse({"ok": True})
