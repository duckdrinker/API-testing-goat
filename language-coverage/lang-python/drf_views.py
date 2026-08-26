"""
Django REST Framework fixtures:
  ASEC-070 (BOLA Phase B), ASEC-075 (BFLA Phase B), ASEC-101 (rate_limit_absence).

See urls.py for the method+path mapping (Django resolves routing across
files, so the detector needs both files to determine each view's endpoint).
"""

from rest_framework.views import APIView
from rest_framework.permissions import IsAuthenticated, IsAdminUser
from rest_framework.response import Response
from rest_framework.throttling import UserRateThrottle


class IsOwnerOrAdmin(IsAuthenticated):
    """Ownership-check permission class referenced by SafeOrderDetailView."""


class OrderDetailView(APIView):
    """ASEC-070: BOLA Phase B, check absent — generic IsAuthenticated only."""
    permission_classes = [IsAuthenticated]

    def get(self, request, id):
        return Response({'order': id})


class SafeOrderDetailView(APIView):
    """ASEC-070: BOLA Phase B, check present — permission_classes = [IsOwnerOrAdmin]."""
    permission_classes = [IsOwnerOrAdmin]

    def get(self, request, id):
        return Response({'order': id})


class AdminReportView(APIView):
    """ASEC-075: BFLA Phase B, check absent — /admin/ path, generic auth only."""
    permission_classes = [IsAuthenticated]

    def get(self, request):
        return Response({'report': '...'})


class SafeAdminReportView(APIView):
    """ASEC-075: BFLA Phase B, check present — permission_classes = [IsAdminUser]."""
    permission_classes = [IsAdminUser]

    def get(self, request):
        return Response({'report': '...'})


class FeedbackThrottle(UserRateThrottle):
    rate = '100/day'


class FeedbackView(APIView):
    """ASEC-101 negative: throttle_classes configured -> no rate_limit_absence."""
    permission_classes = [IsAuthenticated]
    throttle_classes = [FeedbackThrottle]

    def post(self, request):
        return Response(status=202)


class SupportTicketDrfView(APIView):
    """ASEC-101 positive: no throttle_classes and no DEFAULT_THROTTLE_RATES -> rate_limit_absence."""
    permission_classes = [IsAuthenticated]

    def post(self, request):
        return Response(status=202)
