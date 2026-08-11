from django.urls import path
from . import drf_views as views

urlpatterns = [
    path('api/orders/<str:id>/', views.OrderDetailView.as_view()),
    path('api/orders/safe/<str:id>/', views.SafeOrderDetailView.as_view()),
    path('api/admin/report/', views.AdminReportView.as_view()),
    path('api/admin/safe-report/', views.SafeAdminReportView.as_view()),
    path('api/feedback/', views.FeedbackView.as_view()),
    path('api/support-tickets-drf/', views.SupportTicketDrfView.as_view()),
]
