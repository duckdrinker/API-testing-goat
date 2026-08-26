"""
FastAPI fixtures:
  ASEC-056/057 (excessive_data_exposure), ASEC-064/065 (pii_leak_in_response),
  ASEC-094 (mass_assignment), ASEC-087 (jwt_misconfiguration).
  API Discovery (product-backlog#4017, sub-issue #4414): see get_item_v2()
  at the bottom of this file.
"""

from fastapi import Cookie, Depends, FastAPI, Header, Path, Query, Security
from fastapi.security import OAuth2PasswordBearer
from pydantic import BaseModel
import jwt

app = FastAPI()


class UserProfileResponse(BaseModel):
    id: str
    name: str
    email: str
    ssn: str  # unreferenced by the request, sensitivity-tagged -> EDE (056) + pii_leak (064)


class UserSummaryResponse(BaseModel):
    id: str
    name: str  # only operation-relevant fields -> EDE negative (057) / pii_leak negative (065)


class UserProfileUpdateRequest(BaseModel):
    name: str
    email: str
    role: str  # binds through to the persistent entity -> mass_assignment (094)
    enabledPassword: bool  # credential-shaped name but boolean type -> must NOT be sensitivity-tagged


class UserProfileSafeUpdateRequest(BaseModel):
    name: str
    email: str  # no bindable protected field -> mass_assignment negative


@app.get("/api/users/profile")
def get_profile(ssn: str):
    return UserProfileResponse(id="1", name="Jane Doe", email="jane@example.com", ssn=ssn)


@app.get("/api/users/{id}/summary")
def get_summary(id: str):
    return UserSummaryResponse(id=id, name="Jane Doe")


@app.put("/api/users/profile")
def update_profile(update: UserProfileUpdateRequest):
    return {"id": "1", "name": update.name, "email": update.email, "role": update.role, "enabledPassword": update.enabledPassword}


@app.put("/api/users/{id}/safe-update")
def safe_update(id: str, update: UserProfileSafeUpdateRequest):
    return {"id": id, "name": update.name, "email": update.email}


def verify_token(token: str):
    """ASEC-087: verify_signature disabled -> jwt_misconfiguration, severity Critical."""
    return jwt.decode(token, options={"verify_signature": False})


# --- API Discovery fixtures (product-backlog#4017, sub-issue #4414) --------

class ItemResponse(BaseModel):
    id: str
    name: str


def get_current_user(token: str = Header(None)):
    """Dependency stub for Depends(get_current_user) -- resolves the caller
    from a bearer token; injected via Depends(), never treated as a plain
    endpoint parameter."""
    return {"token": token}


oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")


@app.get(
    "/api/items/{item_id}",
    operation_id="get_item_v2",
    tags=["items"],
    deprecated=True,
    status_code=200,
)
def get_item_v2(
    item_id: str = Path(...),
    limit: int = Query(default=10, le=100),
    q: str = Query(..., min_length=1),
    x_request_id: str = Header(None),
    session_id: str = Cookie(None),
    current_user: dict = Depends(get_current_user),
    token_scopes: dict = Security(oauth2_scheme, scopes=["read:items"]),
) -> ItemResponse:
    """
    Exercises, in a single endpoint: Path(...) (required PATH), Query(...)
    both optional (default=10, le=100) and required (..., min_length=1),
    Header(None), Cookie(None), Depends(get_current_user) (dependency
    injection, not a parameter), Security(oauth2_scheme, scopes=[...])
    (oauth2-scopes security requirement), plus operation_id=, tags=[...],
    deprecated=True, status_code=200 on the decorator, and an explicit
    return-type annotation (-> ItemResponse).
    """
    return ItemResponse(id=item_id, name="widget")
