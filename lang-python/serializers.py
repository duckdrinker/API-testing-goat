"""
DRF serializers for the API Discovery fixtures (framework endpoint detection).

See views.py for how each serializer is bound to a request or a response --
the detector distinguishes the two by the presence of the `data=` kwarg
(`Serializer(data=request.data)` is a request binding; `Serializer(obj)` /
`Serializer(qs, many=True)` is a response binding and the only case that
should be surfaced as a DataObject attached to the endpoint).

product-backlog#4017, sub-issue #4441 (Django/DRF).
"""

from rest_framework import serializers


class ItemSerializer(serializers.ModelSerializer):
    """Explicit class-level fields *and* Meta.fields -- exercises both sources
    the detector merges when building the endpoint's DataObject. This fixture
    is source-only (see README.md), so Meta intentionally has no `model`."""
    name = serializers.CharField()
    price = serializers.DecimalField(max_digits=10, decimal_places=2)
    is_active = serializers.BooleanField(default=True)

    class Meta:
        fields = ('id', 'name', 'price', 'is_active', 'created_at')


class EmptySerializer(serializers.Serializer):
    """No fields at all -- must NOT produce any DataObject when analyzed."""
    pass
