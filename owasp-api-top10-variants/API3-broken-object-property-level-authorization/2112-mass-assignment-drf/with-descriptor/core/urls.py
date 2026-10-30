from django.urls import include, path
from rest_framework import routers

from .views import AllowlistView, BlanketCopyView, DeclaredBindView, ModelBoundViewSet, SafeViewSet

router = routers.DefaultRouter()
router.register(r"data-sources", ModelBoundViewSet, basename="datasource")   # V5: router idiom
router.register(r"safe-sources", SafeViewSet, basename="safesource")

urlpatterns = [
    path("", include(router.urls)),
    path("declared/", DeclaredBindView.as_view()),                   # V1
    path("blanket/<int:pk>/", BlanketCopyView.as_view()),            # V2
    path("model-bound/", ModelBoundViewSet.as_view({"post": "create", "get": "list"})),  # V3/V4 path()
    path("allowlist/<int:pk>/", AllowlistView.as_view()),            # NEGATIVE
]
