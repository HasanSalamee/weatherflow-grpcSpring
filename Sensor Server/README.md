# Weather gRPC example (Python)

This folder contains a small example using the generated `weather_pb2.py` and
`weather_pb2_grpc.py` from `weather.proto`.

Files:
- `server_example.py` — simple gRPC server that streams `WeatherStats` and `AlertMessage`.
- `client_example.py` — client that connects to the server and prints received messages.
- `requirements.txt` — Python dependencies.

Quick run (in two terminals):

1. Install deps (optional, recommended inside a venv):

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

2. Start the server:

```bash
python3 server_example.py
```

3. Run the client (in another terminal):

```bash
python3 client_example.py
```
