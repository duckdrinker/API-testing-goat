"""
Flask fixtures: ASEC-080 (cors_misconfiguration), ASEC-100 (rate_limit_absence).
API Discovery (product-backlog#4017, sub-issue #4415): see the Blueprint,
converters, endpoint= override, security decorators and MethodView class
added below. See test-plan-apisec-framework-discovery.html.
"""

from flask import Blueprint, Flask, request
from flask.views import MethodView
from flask_cors import CORS
from flask_login import login_required
from flask_jwt_extended import jwt_required
from flask_security import roles_required

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


# --- API Discovery fixtures (product-backlog#4017, sub-issue #4415) --------
# See test-plan-apisec-framework-discovery.html.

# Flask 2.x shortcut decorators (@app.get/@app.post), instead of the classic
# @app.route(methods=[...]) form used above.
@app.get('/api/flask-items/<int:item_id>')
def get_item(item_id):
    """<int:...> converter -> PATH parameter typed int."""
    return {'id': item_id}


@app.post('/api/items')
@login_required
def create_item():
    """@login_required -> framework discovery (security)."""
    return {'created': True}, 201


# Multiple converters on the same route, plus classic @app.route(methods=[...])
# with more than one HTTP method on the same view function.
@app.route('/api/files/<uuid:owner_id>/<path:filepath>', methods=['GET', 'DELETE'])
@jwt_required
def file_by_owner(owner_id, filepath):
    """<uuid:...> and <path:...> converters; @jwt_required -> framework
    discovery (security); two HTTP methods synthesized from one decorator."""
    return {'owner_id': str(owner_id), 'filepath': filepath}


# endpoint= override of the operationId.
@app.route('/api/legacy-search', endpoint='search_endpoint')
def legacy_search():
    return {'results': []}


# Blueprint with its own url_prefix, registered further down.
items_bp = Blueprint('items_bp', __name__, url_prefix='/api/v2/items')


@items_bp.route('/<slug:item_slug>')
def get_item_by_slug(item_slug):
    """<slug:...> converter on a Blueprint route -> the Blueprint's
    url_prefix must be applied to the discovered path."""
    return {'slug': item_slug}


@items_bp.get('/search')
@roles_required('admin')
def search_items():
    """Flask 2.x shortcut on a Blueprint; @roles_required('admin') ->
    framework discovery (security)."""
    return {'results': []}


app.register_blueprint(items_bp)


class ItemAPI(MethodView):
    """Class-based view (MethodView): each HTTP method on the class becomes
    its own endpoint, operationId "ItemAPI.<method>". Known, documented
    behaviour (not a bug to fix here): the detector does not resolve
    add_url_rule()'s actual path, so all of these are reported at path "/"
    rather than at the real mounted path below."""

    def get(self):
        return {'items': []}

    @login_required
    def post(self):
        """@login_required on a MethodView method -> framework discovery
        (security)."""
        return {'created': True}, 201

    @roles_required('admin')
    def delete(self):
        return '', 204


app.add_url_rule('/api/v2/items-legacy', view_func=ItemAPI.as_view('item_api'))
