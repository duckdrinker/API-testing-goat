from rest_framework.views import APIView
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response


class SupportTicketDrfView(APIView):
    """Isolated test: no throttle_classes and no DEFAULT_THROTTLE_RATES -> rate_limit_absence."""
    permission_classes = [IsAuthenticated]

    def post(self, request):
        return Response(status=202)
