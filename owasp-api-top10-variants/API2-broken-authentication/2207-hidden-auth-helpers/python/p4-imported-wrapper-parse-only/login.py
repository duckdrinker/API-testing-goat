from django.contrib.auth import authenticate, login
from django.http import JsonResponse


def login_view(request):
    user = authenticate(request, username=request.POST["username"], password=request.POST["password"])
    if user is None:
        return JsonResponse({"ok": False}, status=401)
    login(request, user)
    return JsonResponse({"ok": True})
