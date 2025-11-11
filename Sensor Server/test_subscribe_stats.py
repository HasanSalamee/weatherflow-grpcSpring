"""Test client for SubscribeToStats (server-streaming).

Run:
    python3 test_subscribe_stats.py [host] [port] [region]

Defaults: localhost 50051 Gulf
"""
import sys
import grpc
import weather_pb2 as pb
import weather_pb2_grpc as pb_grpc


def main():
    host = sys.argv[1] if len(sys.argv) > 1 else "localhost"
    port = int(sys.argv[2]) if len(sys.argv) > 2 else 50051
    region = sys.argv[3] if len(sys.argv) > 3 else "Gulf"

    target = f"{host}:{port}"
    with grpc.insecure_channel(target) as channel:
        stub = pb_grpc.AnalyticsServiceStub(channel)
        print(f"Requesting stats for region: {region} from {target}")
        try:
            for stats in stub.SubscribeToStats(pb.ReportRequest(region=region)):
                print(f"Stats: avg_temp={stats.avg_temp:.2f}, max_temp={stats.max_temp:.2f}, total_alerts={stats.total_alerts}")
        except grpc.RpcError as e:
            print(f"RPC error: {e}")


if __name__ == '__main__':
    main()
