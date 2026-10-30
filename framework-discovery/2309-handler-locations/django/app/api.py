from rest_framework import viewsets
from rest_framework.decorators import api_view
from rest_framework.response import Response


@api_view(["GET"])
def api_function(request):
    return Response({"ok": True})


class BookViewSet(viewsets.ViewSet):
    def list(self, request):
        return Response([])

    def retrieve(self, request, pk=None):
        return Response({"pk": pk})
