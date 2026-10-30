from django.urls import path, include
from rest_framework.routers import DefaultRouter

from app import views, api

router = DefaultRouter()
router.register(r"books", api.BookViewSet, basename="book")

urlpatterns = [
    path("plain/", views.plain_view),
    path("cbv/", views.HelloView.as_view()),
    path("apiview/", api.api_function),
    path("", include(router.urls)),
]
