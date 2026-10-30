import requests
from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework.views import APIView

from .models import Widget
from .serializers import RemoteImportSerializer, WidgetSerializer


class WidgetViewSet(viewsets.ModelViewSet):
    """NEGATIVE: only input named `url` is the read-only self-link; no HTTP fetch."""

    queryset = Widget.objects.all()
    serializer_class = WidgetSerializer


class RemoteImportView(APIView):
    """POSITIVE: user-controlled URL fetched by the handler."""

    def post(self, request):
        data = RemoteImportSerializer(data=request.data)
        data.is_valid(raise_exception=True)
        resp = requests.get(data.validated_data["url"], timeout=5)
        return Response({"status": resp.status_code, "size": len(resp.content)})


class RemoteImportViewSet(viewsets.ViewSet):
    """POSITIVE (serializer_class linking path): URL field + fetch in the handler."""

    serializer_class = RemoteImportSerializer

    def create(self, request):
        data = RemoteImportSerializer(data=request.data)
        data.is_valid(raise_exception=True)
        resp = requests.get(data.validated_data["url"], timeout=5)
        return Response({"status": resp.status_code})


class RemoteLabelView(APIView):
    """NEGATIVE: URL-shaped writable input, but the handler never fetches it."""

    def post(self, request):
        data = RemoteImportSerializer(data=request.data)
        data.is_valid(raise_exception=True)
        return Response({"label": data.validated_data.get("label"), "ref": data.validated_data["url"]})


class RemoteNoteView(APIView):
    """NEGATIVE (clean): URL-shaped writable input, no `.get(...)` call anywhere in the handler."""

    def post(self, request):
        data = RemoteImportSerializer(data=request.data)
        data.is_valid(raise_exception=True)
        return Response({"ref": data.validated_data["url"]})
