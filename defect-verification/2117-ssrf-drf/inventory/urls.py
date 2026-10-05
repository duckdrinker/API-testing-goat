from django.urls import include, path
from rest_framework import routers

from .views import RemoteImportView, RemoteImportViewSet, RemoteLabelView, RemoteNoteView, WidgetViewSet

router = routers.DefaultRouter()
router.register(r"widgets", WidgetViewSet, basename="widget")
router.register(r"imports-vs", RemoteImportViewSet, basename="import-vs")

urlpatterns = [
    path("", include(router.urls)),
    path("imports/", RemoteImportView.as_view()),
    path("labels/", RemoteLabelView.as_view()),
    path("notes/", RemoteNoteView.as_view()),
]
