import json

import uvicorn
from fastapi import FastAPI, HTTPException, Request

from aco_code.aco_optimizer import aco

app = FastAPI(title='AgroHub Travel Service')


@app.get("/")
def hello():
    return {"message": "Hello This is AgroHub Travel Service"}


@app.post('/generate-routes/')
async def generate_routes(request: Request):
    string_body = await request.body()
    input_body = json.loads(string_body)

    if input_body['secret'] == 'nOa2Xj7woa':
        try:
            res = aco(input_body)
            return res
        except Exception as e:
            raise HTTPException(status_code=400, detail="Invalid input")
    else:
        raise HTTPException(status_code=403, detail="Invalid secret")


if __name__ == "__main__":
    uvicorn.run(app, host="127.0.0.1", port=5000)
