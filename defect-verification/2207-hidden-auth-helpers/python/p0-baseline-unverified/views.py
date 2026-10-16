from django.http import JsonResponse
from .models import User


def promote(request):
    user = User.objects.get(pk=request.POST["user_id"])
    user.is_staff = True
    user.save()
    return JsonResponse({"ok": True})
