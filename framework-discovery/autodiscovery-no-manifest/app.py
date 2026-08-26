"""
Flask endpoint with ZERO manifest files anywhere in this project — no
requirements.txt, no pyproject.toml, no package.json, no pom.xml, nothing.

See README.md: this app should still be discovered via the
"no evidence at all -> load all detectors" fallback.
"""

from flask import Flask

app = Flask(__name__)


@app.route("/api/ping")
def ping():
    return {"status": "ok"}
