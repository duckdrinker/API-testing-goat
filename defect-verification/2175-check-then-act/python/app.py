from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import text

app = Flask(__name__)
app.config["SQLALCHEMY_DATABASE_URI"] = "sqlite://"
db = SQLAlchemy(app)


class Coupon(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    code = db.Column(db.String)
    status = db.Column(db.String)


class Redemption(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    code = db.Column(db.String)


def mark_used(coupon):
    coupon.status = "used"
    db.session.commit()


# c1: unguarded check -> write
@app.route("/coupons/<int:cid>/redeem-unguarded", methods=["POST"])
def redeem_unguarded(cid):
    coupon = Coupon.query.get(cid)
    if coupon.status == "open":
        coupon.status = "used"
        db.session.commit()
    return "ok"


# c2: check in the handler, write in a helper
@app.route("/coupons/<int:cid>/redeem-split", methods=["POST"])
def redeem_split(cid):
    coupon = Coupon.query.get(cid)
    if coupon.status == "open":
        mark_used(coupon)
    return "ok"


# c3: atomic conditional UPDATE
@app.route("/coupons/<int:cid>/redeem-atomic", methods=["POST"])
def redeem_atomic(cid):
    coupon = Coupon.query.get(cid)
    if coupon.status == "open":
        db.session.execute(text("UPDATE coupon SET status = 'used' WHERE id = :id AND status = 'open'"), {"id": cid})
        db.session.commit()
    return "ok"


# c4: delete of the row just checked
@app.route("/coupons/<int:cid>/delete-after-check", methods=["POST"])
def delete_after_check(cid):
    coupon = Coupon.query.get(cid)
    if coupon.status == "expired":
        db.session.delete(coupon)
        db.session.commit()
    return "ok"


# c6: already-done write
@app.route("/coupons/<int:cid>/already-done-write", methods=["POST"])
def already_done_write(cid):
    coupon = Coupon.query.get(cid)
    if coupon.status == "used":
        coupon.status = "used"
        db.session.commit()
    return "ok"


# c7: new row saved after an existence check on the value it carries
@app.route("/coupons/<code>/register-redemption", methods=["POST"])
def register_redemption(code):
    existing = Redemption.query.filter_by(code=code).first()
    if existing is None:
        db.session.add(Redemption(code=code))
        db.session.commit()
    return "ok"
