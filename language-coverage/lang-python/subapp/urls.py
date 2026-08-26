"""
Child urlconf included from the top-level urls.py via
include('subapp.urls'), mounted under the 'sub/' prefix -- exercises the
detector's cross-file include() resolution and mount-prefix propagation.

product-backlog#4017, sub-issue #4441 (Django/DRF).
"""

from django.urls import path

from . import views as subapp_views

urlpatterns = [
    path('items/<int:id>/', subapp_views.ItemView.as_view()),
    path('ping/', subapp_views.PingView.as_view()),
]
