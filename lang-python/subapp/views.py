"""
Sub-app views for the include() cross-file resolution fixture.

See ../urls.py (path('sub/', include('subapp.urls'))) and this package's
own urls.py -- the detector must resolve include() across files and apply
the parent's 'sub/' mount prefix to these child routes.

product-backlog#4017, sub-issue #4441 (Django/DRF).
"""

from rest_framework.response import Response
from rest_framework.views import APIView


class ItemView(APIView):
    """Bound (via subapp/urls.py, mounted under 'sub/' by the parent
    urls.py) to sub/items/<int:id>/."""

    def get(self, request, id):
        return Response({'id': id, 'source': 'subapp'})


class PingView(APIView):
    """Bound to sub/ping/ -- a simple no-parameter route in the sub-app."""

    def get(self, request):
        return Response({'pong': True})
