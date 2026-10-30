from rest_framework import serializers

from .models import Widget


class BaseModelSerializer(serializers.ModelSerializer):
    # Read-only self link inherited by every model serializer (NetBox idiom).
    url = serializers.HyperlinkedIdentityField(view_name="widget-detail")


class WidgetSerializer(BaseModelSerializer):
    class Meta:
        model = Widget
        fields = ("id", "url", "name", "description")


class RemoteImportSerializer(serializers.Serializer):
    # Genuine, writable, URL-shaped input.
    url = serializers.URLField()
    label = serializers.CharField(required=False)
