from django.urls import path
from . import drf_views as views

urlpatterns = [
    path('api/support-tickets-drf/', views.SupportTicketDrfView.as_view()),
]
