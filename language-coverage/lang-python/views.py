"""
Django / DRF views for the API Discovery fixtures (framework endpoint
detection): function-based @api_view, DRF generic concrete views,
ViewSets + @action, and request/response serializer binding.

See urls.py for routing (re_path with named groups, path() converters,
router.register() for the ViewSets, and the subapp/ include()) and
serializers.py for the serializer classes referenced below.

product-backlog#4017, sub-issue #4441 (Django/DRF).
"""

from rest_framework import status
from rest_framework.authentication import TokenAuthentication
from rest_framework.decorators import (
    action,
    api_view,
    authentication_classes,
    permission_classes,
)
from rest_framework.generics import ListCreateAPIView, RetrieveUpdateDestroyAPIView
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework.viewsets import ModelViewSet, ReadOnlyModelViewSet

from .serializers import ItemSerializer


# --- re_path with named groups + name= override -----------------------------

class ArchiveView(APIView):
    """Targeted by re_path(r'^archive/(?P<year>...)/(?P<month>...)/$',
    name='archive') in urls.py -- exercises named regex groups as PATH
    parameters, and the name= kwarg as an operationId override."""

    def get(self, request, year, month):
        return Response({'year': year, 'month': month})


# --- path() converters (int already covered by ItemDetailView below) -------

class ItemByTokenView(APIView):
    """Bound to path('items/<uuid:token>/', ...) in urls.py."""

    def get(self, request, token):
        return Response({'token': str(token)})


class ItemBySlugView(APIView):
    """Bound to path('items/<slug:slug>/', ...) in urls.py. Uses
    ItemSerializer for the RESPONSE (many=True, no data= kwarg) -> counts
    as a DataObject linked to this endpoint."""

    def get(self, request, slug):
        items = [{'id': 1, 'name': slug, 'price': '9.99', 'is_active': True}]
        return Response(ItemSerializer(items, many=True).data)


class FileView(APIView):
    """Bound to path('files/<path:filepath>/', ...) in urls.py."""

    def get(self, request, filepath):
        return Response({'filepath': filepath})


# --- function-based view: @api_view + explicit auth/permission decorators --

@api_view(['GET', 'POST'])
@permission_classes([IsAuthenticated])
@authentication_classes([TokenAuthentication])
def item_collection(request):
    """GET+POST via @api_view(['GET', 'POST']), protected by
    @permission_classes([IsAuthenticated]) and
    @authentication_classes([TokenAuthentication]).
    On POST, uses ItemSerializer for the REQUEST body (data=request.data) --
    request-bound usage, not counted as a response DataObject."""
    if request.method == 'POST':
        serializer = ItemSerializer(data=request.data)
        serializer.is_valid()
        return Response(serializer.errors or {'created': True}, status=status.HTTP_201_CREATED)
    return Response({'items': []})


# --- Generic concrete views --------------------------------------------------

class ItemListView(ListCreateAPIView):
    """Generic Concrete View: ListCreateAPIView -> GET (list) + POST (create)
    produced automatically, with no explicit get()/post() methods. Bound to
    path('items/', ...) in urls.py."""
    serializer_class = ItemSerializer
    queryset = []


class ItemDetailView(RetrieveUpdateDestroyAPIView):
    """Generic Concrete View: RetrieveUpdateDestroyAPIView -> GET+PUT+PATCH+
    DELETE produced automatically. Bound to path('items/<int:id>/', ...)
    in urls.py, exercising the <int:...> converter."""
    serializer_class = ItemSerializer
    queryset = []
    lookup_url_kwarg = 'id'


# --- ViewSets -----------------------------------------------------------------

class ItemViewSet(ModelViewSet):
    """Full ModelViewSet -> list/create/retrieve/update/partial_update/
    destroy, plus a custom @action(detail=True, methods=['post'],
    url_path='mark-archived') endpoint. Also registered on a DefaultRouter in
    urls.py -- KNOWN LIMITATION (documented in the issue itself): router.register()
    URL resolution is not traced, so the real router-derived path is not
    expected to appear; these actions are still emitted (path placeholder "/")
    from direct class inspection, independent of any url resolution."""
    serializer_class = ItemSerializer
    queryset = []

    @action(detail=True, methods=['post'], url_path='mark-archived')
    def mark_archived(self, request, pk=None):
        return Response({'id': pk, 'archived': True})


class ItemReadOnlyViewSet(ReadOnlyModelViewSet):
    """ReadOnlyModelViewSet -> only list + retrieve should be produced,
    never create/update/destroy."""
    serializer_class = ItemSerializer
    queryset = []
