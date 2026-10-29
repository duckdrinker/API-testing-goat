from django.http import JsonResponse
from django.views import View


def plain_view(request):
    data = {"plain": True}
    return JsonResponse(data)


class HelloView(View):
    def get(self, request):
        return JsonResponse({"hello": True})
