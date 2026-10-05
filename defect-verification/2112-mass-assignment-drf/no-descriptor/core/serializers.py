from rest_framework import serializers

from .models import DataSource


class DataSourceSerializer(serializers.ModelSerializer):
    """Writable: exposes enabled / owner / is_admin (protected attributes)."""

    class Meta:
        model = DataSource
        fields = ("id", "name", "enabled", "owner", "is_admin")


class SafeDataSourceSerializer(serializers.ModelSerializer):
    """Allowlist: protected attributes are read-only."""

    class Meta:
        model = DataSource
        fields = ("id", "name", "enabled", "owner", "is_admin")
        read_only_fields = ("enabled", "owner", "is_admin")


class DeclaredSerializer(serializers.Serializer):
    name = serializers.CharField()
    enabled = serializers.BooleanField()
    owner = serializers.IntegerField()
