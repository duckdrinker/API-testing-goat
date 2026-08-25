from django.urls import path, re_path, include
from rest_framework.routers import DefaultRouter
from . import drf_views as views
from . import views as new_views

# --- API Discovery fixtures (product-backlog#4017, sub-issue #4441) --------
# ViewSets are wired via a router, same as urlpatterns' regular entries.
router = DefaultRouter()
router.register(r'items-vs', new_views.ItemViewSet, basename='item-viewset')
router.register(r'items-ro', new_views.ItemReadOnlyViewSet, basename='item-readonly')

urlpatterns = [
    path('api/orders/<str:id>/', views.OrderDetailView.as_view()),
    path('api/orders/safe/<str:id>/', views.SafeOrderDetailView.as_view()),
    path('api/admin/report/', views.AdminReportView.as_view()),
    path('api/admin/safe-report/', views.SafeAdminReportView.as_view()),
    path('api/feedback/', views.FeedbackView.as_view()),
    path('api/support-tickets-drf/', views.SupportTicketDrfView.as_view()),

    # --- API Discovery fixtures (product-backlog#4017, sub-issue #4441) ---

    # re_path with named regex groups (-> PATH params) + name= as an
    # operationId override.
    re_path(r'^archive/(?P<year>[0-9]{4})/(?P<month>[0-9]{2})/$',
            new_views.ArchiveView.as_view(), name='archive'),

    # path() converters: int / uuid / slug / path (only <str:id> existed
    # above before this fixture).
    path('items/', new_views.ItemListView.as_view()),
    path('items/<int:id>/', new_views.ItemDetailView.as_view()),
    path('items/<uuid:token>/', new_views.ItemByTokenView.as_view()),
    path('items/<slug:slug>/', new_views.ItemBySlugView.as_view()),
    path('files/<path:filepath>/', new_views.FileView.as_view()),

    # function-based @api_view view (GET+POST, explicit auth/permission
    # decorators).
    path('items/collection/', new_views.item_collection),

    # include() cross-file resolution: subapp's own urls.py is resolved and
    # its routes get the 'sub/' mount prefix applied.
    path('sub/', include('subapp.urls')),
] + router.urls
