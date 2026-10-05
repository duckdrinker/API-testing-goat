from django.contrib.auth import get_user_model
from django.db import models


class DataSource(models.Model):
    name = models.CharField(max_length=100)
    enabled = models.BooleanField(default=True)
    owner = models.ForeignKey(get_user_model(), null=True, on_delete=models.SET_NULL)
    is_admin = models.BooleanField(default=False)
