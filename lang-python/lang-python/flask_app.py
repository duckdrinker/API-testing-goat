"""
Flask fixtures: ASEC-080 (cors_misconfiguration), ASEC-100 (rate_limit_absence).
"""

from flask import Flask, request
from flask_cors import CORS

app = Flask(__name__)

# ASEC-080: CORS misconfiguration — wildcard origin combined with credentials
# (the dangerous combination), via flask-cors.
CORS(app, origins='*', supports_credentials=True)


def require_auth():
    return bool(request.headers.get('Authorization'))


@app.route('/api/support-tickets', methods=['POST'])
def create_ticket():
    """
    ASEC-100: authenticated, user-input-accepting endpoint with no
    flask-limiter decorator/middleware anywhere in this app -> rate_limit_absence.
    """
    if not require_auth():
        return {'error': 'unauthorized'}, 401
    request.get_json()
    return {'status': 'created'}, 201
