# 🌤️ WeatherFlow gRPC - Distributed Weather Monitoring System

A multi-language distributed weather monitoring system built with gRPC, featuring real-time data streaming, analytics, and web dashboard.

## 📋 System Overview

This project demonstrates a distributed system using gRPC with three different programming languages working together seamlessly:

- **Python** - Sensor data generator
- **Go** - Analytics and data processing service  
- **Java** - Client with web dashboard

## 🏗️ System Architecture

```
┌─────────────┐    gRPC Streaming    ┌─────────────┐    gRPC Server Streaming    ┌─────────────┐
│   Python    │ ───────────────────► │     Go      │ ──────────────────────────► │    Java     │
│   Sensor    │                      │  Analytics  │                            │  Dashboard  │
│   Client    │                      │   Service   │                            │   Client    │
└─────────────┘                      └─────────────┘                            └─────────────┘
     │                                      │                                            │
     │ Sensor Data                          │ Processed Statistics                       │ Web Interface
     │ (Client Streaming)                   │ (Server Streaming)                         │ (HTTP Dashboard)
```

## 🚀 Features

### 🔄 Real-time Data Flow
- **Python Sensor**: Generates realistic weather data with random values
- **Go Analytics**: Processes incoming data, calculates statistics, and manages alerts
- **Java Dashboard**: Displays live data through a web interface

### 📊 Core Components

| Component | Language | Role | Protocol |
|-----------|----------|------|----------|
| Sensor Client | Python | Data generation & streaming | gRPC Client Streaming |
| Analytics Service | Go | Data processing & distribution | gRPC Server Streaming |
| Dashboard Client | Java | Data visualization & web interface | HTTP + gRPC |

### 🌐 Web Dashboard
- Real-time weather statistics display
- Live alerts and notifications
- Auto-refresh every 5 seconds
- Responsive design
- RESTful API endpoints

## 🛠️ Technology Stack

- **gRPC** - High-performance RPC framework
- **Protocol Buffers** - Interface definition and data serialization
- **Python** - Sensor data simulation
- **Go** - Backend service and analytics
- **Java** - Client application with embedded web server
- **HTTP/2** - Efficient communication protocol

## 📥 Installation & Setup

### Prerequisites

- **Python 3.7+**
- **Go 1.19+**
- **Java 17+**
- **Git**

### 1. Clone the Repository

```bash
git clone https://github.com/HasanSalamee/weatherflow-grpcSpring.git
cd weatherflow-grpcSpring
```

### 2. Generate gRPC Code

```bash
# Generate Python stubs
python -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. weather.proto

# Generate Go stubs
protoc --go_out=. --go-grpc_out=. weather.proto

# Generate Java stubs
protoc --java_out=src --grpc-java_out=src weather.proto
```

## 🎯 Quick Start

### Step 1: Start the Go Analytics Service

```bash
cd go-service
go run main.go -addr ":50052"
```

**Expected Output:**
```
🚀 Server listening on :50052
📊 Supports both services: SensorService and AnalyticsService
🔄 Ready to receive batches and send data immediately
⏰ Waiting for connections...
```

### Step 2: Start the Java Dashboard Client

```bash
cd java-client
./compile.sh  # Or manually compile with javac
java -cp "bin:lib/*" com.example.javaServer.Client.Main
```

**Expected Output:**
```
==================================================
🌤️ Distributed Weather Monitoring System Client
==================================================
📍 Connecting to: 192.168.96.202:50052
🚀 Starting weather monitoring client...
📊 Subscribing to weather statistics...
🚨 Subscribing to weather alerts...
🌐 Dashboard running at: http://localhost:8080
```

### Step 3: Start the Python Sensor

```bash
cd python-sensor
python test_stream_sensor.py
```

**Expected Output:**
```
🌤️ Weather Sensor Client - Random Data
📍 Target: 192.168.96.202:50052
🔄 Sending 10 random readings...
[14:30:25] 📤 Sending reading #1:
   🆔 Sensor: python-sensor-5678
   🌡️ Temperature: 23.45°C
   💧 Humidity: 67.89%
   📊 Pressure: 1012.34 hPa
```

## 🌐 Access the Dashboard

Open your browser and navigate to:
```
http://localhost:8080
```

The dashboard will display:
- 📊 Live weather statistics
- 🚨 Real-time weather alerts
- 📈 Historical data trends

## 📡 API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/` | GET | Web dashboard interface |
| `/api/stats` | GET | JSON weather statistics |
| `/api/alerts` | GET | JSON weather alerts |
| `/api/latest` | GET | Latest data snapshot |
| `/api/health` | GET | Service health check |

## 🔧 Configuration

### Network Settings

Update the IP addresses in each component:

**Java Client:**
```java
String host = "192.168.96.202";  // Your Go service IP
int port = 50052;
```

**Python Sensor:**
```python
host = "192.168.96.202"  # Your Go service IP
port = 50052
```

**Go Service:**
```go
addr := flag.String("addr", ":50052", "listen address")
```

### Custom Ports

- **Go Service**: 50052 (gRPC)
- **Java Dashboard**: 8080 (HTTP)

## 🐛 Troubleshooting

### Common Issues

1. **Connection Refused**
   - Verify Go service is running
   - Check firewall settings
   - Confirm IP addresses match

2. **No Data in Dashboard**
   - Check Python sensor is sending data
   - Verify gRPC stubs are generated
   - Monitor Go service logs

3. **Compilation Errors**
   - Ensure all dependencies are installed
   - Verify protobuf compiler version
   - Check Java classpath configuration

### Logs and Monitoring

Each component provides detailed logs:
- **Go**: Connection status and data processing
- **Java**: Subscription status and web server
- **Python**: Data generation and transmission

## 📁 Project Structure

```
weatherflow-grpcSpring/
├── go-service/
│   ├── main.go                 # Go analytics service
│   └── weather.pb.go           # Generated Go protobuf
├── java-client/
│   ├── src/                    # Java source code
│   ├── lib/                    # Dependencies
│   └── bin/                    # Compiled classes
├── python-sensor/
│   └── test_stream_sensor.py   # Python sensor client
├── weather.proto               # Protocol buffers definition
└── README.md                   # This file
```

## 🔄 Data Flow Details

### 1. Sensor Data Generation (Python)
- Generates realistic weather readings
- Random temperatures (-10°C to 45°C)
- Random humidity (20% to 95%)
- Random pressure (980hPa to 1040hPa)
- Streams data via gRPC client streaming

### 2. Data Processing (Go)
- Receives sensor data batches
- Calculates averages and statistics
- Manages client subscriptions
- Sends real-time updates via server streaming

### 3. Visualization (Java)
- Subscribes to analytics service
- Stores historical data
- Provides web dashboard
- Auto-refreshes display

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test all components
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- **Hasan Salamee** - Initial work and system design

## 🙏 Acknowledgments

- gRPC team for the excellent RPC framework
- Protocol Buffers for efficient serialization
- The open-source community for various libraries used

---

**⭐ If you find this project useful, please give it a star!**
