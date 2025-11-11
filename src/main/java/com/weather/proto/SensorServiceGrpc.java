package com.weather.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.0)",
    comments = "Source: weather.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class SensorServiceGrpc {

  private SensorServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "SensorService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.weather.proto.SensorReading,
      com.weather.proto.SensorAck> getStreamSensorDataMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamSensorData",
      requestType = com.weather.proto.SensorReading.class,
      responseType = com.weather.proto.SensorAck.class,
      methodType = io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
  public static io.grpc.MethodDescriptor<com.weather.proto.SensorReading,
      com.weather.proto.SensorAck> getStreamSensorDataMethod() {
    io.grpc.MethodDescriptor<com.weather.proto.SensorReading, com.weather.proto.SensorAck> getStreamSensorDataMethod;
    if ((getStreamSensorDataMethod = SensorServiceGrpc.getStreamSensorDataMethod) == null) {
      synchronized (SensorServiceGrpc.class) {
        if ((getStreamSensorDataMethod = SensorServiceGrpc.getStreamSensorDataMethod) == null) {
          SensorServiceGrpc.getStreamSensorDataMethod = getStreamSensorDataMethod =
              io.grpc.MethodDescriptor.<com.weather.proto.SensorReading, com.weather.proto.SensorAck>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamSensorData"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.weather.proto.SensorReading.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.weather.proto.SensorAck.getDefaultInstance()))
              .setSchemaDescriptor(new SensorServiceMethodDescriptorSupplier("StreamSensorData"))
              .build();
        }
      }
    }
    return getStreamSensorDataMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SensorServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SensorServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SensorServiceStub>() {
        @java.lang.Override
        public SensorServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SensorServiceStub(channel, callOptions);
        }
      };
    return SensorServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SensorServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SensorServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SensorServiceBlockingStub>() {
        @java.lang.Override
        public SensorServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SensorServiceBlockingStub(channel, callOptions);
        }
      };
    return SensorServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SensorServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SensorServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SensorServiceFutureStub>() {
        @java.lang.Override
        public SensorServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SensorServiceFutureStub(channel, callOptions);
        }
      };
    return SensorServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     * <pre>
     * Sensors open a client-stream to send many SensorReading messages.
     * </pre>
     */
    default io.grpc.stub.StreamObserver<com.weather.proto.SensorReading> streamSensorData(
        io.grpc.stub.StreamObserver<com.weather.proto.SensorAck> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getStreamSensorDataMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service SensorService.
   */
  public static abstract class SensorServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SensorServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service SensorService.
   */
  public static final class SensorServiceStub
      extends io.grpc.stub.AbstractAsyncStub<SensorServiceStub> {
    private SensorServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SensorServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SensorServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Sensors open a client-stream to send many SensorReading messages.
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.weather.proto.SensorReading> streamSensorData(
        io.grpc.stub.StreamObserver<com.weather.proto.SensorAck> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncClientStreamingCall(
          getChannel().newCall(getStreamSensorDataMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service SensorService.
   */
  public static final class SensorServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SensorServiceBlockingStub> {
    private SensorServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SensorServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SensorServiceBlockingStub(channel, callOptions);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service SensorService.
   */
  public static final class SensorServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<SensorServiceFutureStub> {
    private SensorServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SensorServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SensorServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_STREAM_SENSOR_DATA = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAM_SENSOR_DATA:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.streamSensorData(
              (io.grpc.stub.StreamObserver<com.weather.proto.SensorAck>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getStreamSensorDataMethod(),
          io.grpc.stub.ServerCalls.asyncClientStreamingCall(
            new MethodHandlers<
              com.weather.proto.SensorReading,
              com.weather.proto.SensorAck>(
                service, METHODID_STREAM_SENSOR_DATA)))
        .build();
  }

  private static abstract class SensorServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SensorServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.weather.proto.WeatherProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("SensorService");
    }
  }

  private static final class SensorServiceFileDescriptorSupplier
      extends SensorServiceBaseDescriptorSupplier {
    SensorServiceFileDescriptorSupplier() {}
  }

  private static final class SensorServiceMethodDescriptorSupplier
      extends SensorServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SensorServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SensorServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SensorServiceFileDescriptorSupplier())
              .addMethod(getStreamSensorDataMethod())
              .build();
        }
      }
    }
    return result;
  }
}
