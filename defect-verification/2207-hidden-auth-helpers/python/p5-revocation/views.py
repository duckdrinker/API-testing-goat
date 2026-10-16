from django.http import JsonResponse
from .models import User


def demote(request):
    user = User.objects.get(pk=request.POST["user_id"])
    user.is_staff = False
    user.save()
    return JsonResponse({"ok": True})
