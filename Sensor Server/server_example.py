("""Simple gRPC server example for the generated weather protos.

Run with:
	python3 server_example.py

It exposes `AnalyticsService` on localhost:50051 and streams a few
WeatherStats and AlertMessage messages for demo/testing.
""")

import time
import concurrent.futures
import grpc

import weather_pb2 as pb
import weather_pb2_grpc as pb_grpc


class AnalyticsService(pb_grpc.AnalyticsServiceServicer):
	def SubscribeToStats(self, request, context):
		# Demo: stream 6 stats for the requested region
		region = request.region or "(all)"
		print(f"SubscribeToStats called for region: {region}")
		for i in range(6):
			# quickly check for cancellation
			if context.is_active() is False:
				print("Client disconnected, stopping stats stream")
				return
			stats = pb.WeatherStats(
				avg_temp=15.0 + i * 0.7,
				max_temp=18.0 + i * 0.9,
				total_alerts=i,
			)
			yield stats
			time.sleep(0.4)

	def SubscribeToAlerts(self, request, context):
		print("SubscribeToAlerts called")
		alerts = [
			("Riyadh", 45.2, "Heat advisory"),
			("Jeddah", 38.0, "High humidity"),
			("Tabuk", -1.5, "Frost warning"),
		]
		for city, temp, message in alerts:
			if context.is_active() is False:
				print("Client disconnected, stopping alerts stream")
				return
			yield pb.AlertMessage(city=city, temperature=temp, message=message)
			time.sleep(0.5)


def serve(host: str = "0.0.0.0", port: int = 50051):
	server = grpc.server(concurrent.futures.ThreadPoolExecutor(max_workers=10))
	pb_grpc.add_AnalyticsServiceServicer_to_server(AnalyticsService(), server)
	address = f"{host}:{port}"
	server.add_insecure_port(address)
	server.start()
	print(f"gRPC server listening on {address}")
	try:
		server.wait_for_termination()
	except KeyboardInterrupt:
		print("Server stopping...")
		server.stop(0)


if __name__ == "__main__":
	serve()

