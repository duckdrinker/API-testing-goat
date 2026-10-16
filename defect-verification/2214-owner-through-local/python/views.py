"""#2214 - owner bound through a local variable (Django REST framework).

NOT REPORTED cases must stay silent; REPORTED cases must fire
python.user_controlled_primary_key.
"""
from rest_framework.decorators import api_view
from rest_framework.response import Response

from .models import Order


# CTRL-1 REPORTED: unbounded lookup, baseline positive.
@api_view(["GET"])
def ctrl1(request, pk):
    order = Order.objects.get(pk=pk)
    return Response({"id": order.pk})


# CTRL-2 NOT REPORTED: caller written inline in the bound.
@api_view(["GET"])
def ctrl2(request, pk):
    order = Order.objects.get(pk=pk, owner=request.user.username)
    return Response({"id": order.pk})


# A1 NOT REPORTED: bound in the lookup, caller held in a local.
@api_view(["GET"])
def a1(request, pk):
    owner = request.user.username
    order = Order.objects.get(pk=pk, owner=owner)
    return Response({"id": order.pk})


# A2 REPORTED: twin of A1, the local holds a request value.
@api_view(["GET"])
def a2(request, pk):
    owner = request.GET.get("owner")
    order = Order.objects.get(pk=pk, owner=owner)
    return Response({"id": order.pk})


# B1 NOT REPORTED: comparison after the lookup, caller held in a local.
@api_view(["GET"])
def b1(request, pk):
    owner = request.user.username
    order = Order.objects.get(pk=pk)
    if order.owner != owner:
        raise PermissionError("forbidden")
    return Response({"id": order.pk})


# B2 REPORTED: twin of B1, the local holds a request value.
@api_view(["GET"])
def b2(request, pk):
    owner = request.GET.get("owner")
    order = Order.objects.get(pk=pk)
    if order.owner != owner:
        raise PermissionError("forbidden")
    return Response({"id": order.pk})


# C1 REPORTED: local first takes the caller, then is rebound to a request value.
@api_view(["GET"])
def c1(request, pk):
    owner = request.user.username
    owner = request.GET.get("owner")
    order = Order.objects.get(pk=pk, owner=owner)
    return Response({"id": order.pk})


# C2 REPORTED: rebound local, comparison after the lookup.
@api_view(["GET"])
def c2(request, pk):
    owner = request.user.username
    owner = request.GET.get("owner")
    order = Order.objects.get(pk=pk)
    if order.owner != owner:
        raise PermissionError("forbidden")
    return Response({"id": order.pk})


# D1 REPORTED: loop variable, not assigned once from the caller.
@api_view(["GET"])
def d1(request, pk):
    found = None
    for owner in request.GET.getlist("owners"):
        found = Order.objects.get(pk=pk, owner=owner)
    return Response({"id": found.pk})
