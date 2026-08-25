from fastapi import FastAPI

app = FastAPI()


@app.get("/api/ping-fastapi")
def ping():
    return {"status": "ok"}
