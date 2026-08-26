"""
Flask side of the mixed-signals fixture. See package.json for the sibling
Koa (JS) manifest and app.js for the sibling Koa endpoint. Both frameworks'
manifests declare a manifest-hit-level dependency (confidence 0.95 each).
"""

from flask import Flask

app = Flask(__name__)


@app.route("/api/ping-flask")
def ping():
    return {"status": "ok"}
