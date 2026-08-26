"""
Flask endpoint with NO Python manifest anywhere in this project (no
requirements.txt / pyproject.toml). The only manifest in the fixture is
Cargo.toml, declaring the unsupported `actix-web` framework.

See README.md: this app should still be discovered if the "load all
detectors" fallback applies.
"""

from flask import Flask

app = Flask(__name__)


@app.route("/api/ping")
def ping():
    return {"status": "ok"}
