"""Test client for SubscribeToAlerts (server-streaming).

Run:
    python3 test_subscribe_alerts.py [host] [port]

Defaults: localhost 50051
"""
import sys
import grpc
import weather_pb2 as pb
import weather_pb2_grpc as pb_grpc


def main():
    host = sys.argv[1] if len(sys.argv) > 1 else "localhost"
    port = int(sys.argv[2]) if len(sys.argv) > 2 else 50051
    target = f"{host}:{port}"
    with grpc.insecure_channel(target) as channel:
        stub = pb_grpc.AnalyticsServiceStub(channel)
        print(f"Subscribing to alerts from {target}")
        try:
            for alert in stub.SubscribeToAlerts(pb.Empty()):
                print(f"Alert: city={alert.city}, temp={alert.temperature}, msg={alert.message}")
        except grpc.RpcError as e:
            print(f"RPC error: {e}")


if __name__ == '__main__':
    main()
