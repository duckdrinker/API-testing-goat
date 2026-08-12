"""
FastAPI fixtures:
  ASEC-056/057 (excessive_data_exposure), ASEC-064/065 (pii_leak_in_response),
  ASEC-094 (mass_assignment), ASEC-087 (jwt_misconfiguration).
"""

from fastapi import FastAPI
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
