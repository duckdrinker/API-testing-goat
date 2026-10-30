from django.db import models


class Order(models.Model):
    owner = models.CharField(max_length=64)
    status = models.CharField(max_length=16)
