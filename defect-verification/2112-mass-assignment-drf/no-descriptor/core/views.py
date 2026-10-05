from rest_framework import viewsets
from rest_framework.response import Response
from rest_framework.views import APIView

from .models import DataSource
from .serializers import DataSourceSerializer, DeclaredSerializer, SafeDataSourceSerializer


class DeclaredBindView(APIView):
    """V1: Serializer declaring enabled/owner, bound in an APIView."""

    def post(self, request):
        s = DeclaredSerializer(data=request.data)
        s.is_valid(raise_exception=True)
        DataSource.objects.create(**s.validated_data)
        return Response(status=201)


class BlanketCopyView(APIView):
    """V2: untyped blanket copy of every request key onto a persisted record."""

    def post(self, request, pk):
        record = DataSource.objects.get(pk=pk)
        for key in request.data:
            setattr(record, key, request.data[key])
        record.save()
        return Response(status=200)


class ModelBoundViewSet(viewsets.ModelViewSet):
    """V3/V4/V5: model-bound viewset; serializer exposes protected fields."""

    queryset = DataSource.objects.all()
    serializer_class = DataSourceSerializer


class SafeViewSet(viewsets.ModelViewSet):
    """NEGATIVE: protected fields are read_only."""

    queryset = DataSource.objects.all()
    serializer_class = SafeDataSourceSerializer


class AllowlistView(APIView):
    """NEGATIVE: explicit allowlist of writable fields."""

    ALLOWED = ("name",)

    def post(self, request, pk):
        record = DataSource.objects.get(pk=pk)
        for key in self.ALLOWED:
            if key in request.data:
                setattr(record, key, request.data[key])
        record.save()
        return Response(status=200)
