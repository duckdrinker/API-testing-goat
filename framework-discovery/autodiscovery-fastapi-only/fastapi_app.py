"""
Single FastAPI endpoint, no other framework present anywhere in this project.

Only signal for FrameworkProbe: requirements.txt declaring `fastapi`.
See README.md for what this isolates.
"""

from fastapi import FastAPI

app = FastAPI()


@app.get("/api/ping")
def ping():
    return {"status": "ok"}
