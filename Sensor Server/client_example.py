"""Simple gRPC client demo for the generated weather protos.

Run with:
	python3 client_example.py

It connects to localhost:50051, subscribes to stats and alerts, and
prints received messages.
"""

import grpc
import weather_pb2 as pb
import weather_pb2_grpc as pb_grpc


def run_stats(stub: pb_grpc.AnalyticsServiceStub, region: str = "north"):
	print(f"Requesting stats for region: {region}")
	try:
		for stats in stub.SubscribeToStats(pb.ReportRequest(region=region)):
			print(f"Stats: avg_temp={stats.avg_temp:.2f}, max_temp={stats.max_temp:.2f}, total_alerts={stats.total_alerts}")
	except grpc.RpcError as e:
		print(f"Stats RPC error: {e}")


def run_alerts(stub: pb_grpc.AnalyticsServiceStub):
	print("Subscribing to alerts")
	try:
		for alert in stub.SubscribeToAlerts(pb.Empty()):
			print(f"Alert: city={alert.city}, temp={alert.temperature}, msg={alert.message}")
	except grpc.RpcError as e:
		print(f"Alerts RPC error: {e}")


def main():
	target = "192.168.43.219:50051"
	with grpc.insecure_channel(target) as channel:
		stub = pb_grpc.AnalyticsServiceStub(channel)
		run_stats(stub, region="Gulf")
		print("-- stats stream finished --\n")
		run_alerts(stub)


if __name__ == "__main__":
	main()

