package com.weather.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.68.0)",
    comments = "Source: weather.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class AnalyticsServiceGrpc {

  private AnalyticsServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "weather.AnalyticsService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.weather.proto.ReportRequest,
      com.weather.proto.WeatherStats> getSubscribeToStatsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubscribeToStats",
      requestType = com.weather.proto.ReportRequest.class,
      responseType = com.weather.proto.WeatherStats.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.weather.proto.ReportRequest,
      com.weather.proto.WeatherStats> getSubscribeToStatsMethod() {
    io.grpc.MethodDescriptor<com.weather.proto.ReportRequest, com.weather.proto.WeatherStats> getSubscribeToStatsMethod;
    if ((getSubscribeToStatsMethod = AnalyticsServiceGrpc.getSubscribeToStatsMethod) == null) {
      synchronized (AnalyticsServiceGrpc.class) {
        if ((getSubscribeToStatsMethod = AnalyticsServiceGrpc.getSubscribeToStatsMethod) == null) {
          AnalyticsServiceGrpc.getSubscribeToStatsMethod = getSubscribeToStatsMethod =
              io.grpc.MethodDescriptor.<com.weather.proto.ReportRequest, com.weather.proto.WeatherStats>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SubscribeToStats"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.weather.proto.ReportRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.weather.proto.WeatherStats.getDefaultInstance()))
              .setSchemaDescriptor(new AnalyticsServiceMethodDescriptorSupplier("SubscribeToStats"))
              .build();
        }
      }
    }
    return getSubscribeToStatsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.weather.proto.Empty,
      com.weather.proto.AlertMessage> getSubscribeToAlertsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SubscribeToAlerts",
      requestType = com.weather.proto.Empty.class,
      responseType = com.weather.proto.AlertMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<com.weather.proto.Empty,
      com.weather.proto.AlertMessage> getSubscribeToAlertsMethod() {
    io.grpc.MethodDescriptor<com.weather.proto.Empty, com.weather.proto.AlertMessage> getSubscribeToAlertsMethod;
    if ((getSubscribeToAlertsMethod = AnalyticsServiceGrpc.getSubscribeToAlertsMethod) == null) {
      synchronized (AnalyticsServiceGrpc.class) {
        if ((getSubscribeToAlertsMethod = AnalyticsServiceGrpc.getSubscribeToAlertsMethod) == null) {
          AnalyticsServiceGrpc.getSubscribeToAlertsMethod = getSubscribeToAlertsMethod =
              io.grpc.MethodDescriptor.<com.weather.proto.Empty, com.weather.proto.AlertMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SubscribeToAlerts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.weather.proto.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.weather.proto.AlertMessage.getDefaultInstance()))
              .setSchemaDescriptor(new AnalyticsServiceMethodDescriptorSupplier("SubscribeToAlerts"))
              .build();
        }
      }
    }
    return getSubscribeToAlertsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AnalyticsServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AnalyticsServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AnalyticsServiceStub>() {
        @java.lang.Override
        public AnalyticsServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AnalyticsServiceStub(channel, callOptions);
        }
      };
    return AnalyticsServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AnalyticsServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AnalyticsServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AnalyticsServiceBlockingStub>() {
        @java.lang.Override
        public AnalyticsServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AnalyticsServiceBlockingStub(channel, callOptions);
        }
      };
    return AnalyticsServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AnalyticsServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AnalyticsServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AnalyticsServiceFutureStub>() {
        @java.lang.Override
        public AnalyticsServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AnalyticsServiceFutureStub(channel, callOptions);
        }
      };
    return AnalyticsServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void subscribeToStats(com.weather.proto.ReportRequest request,
        io.grpc.stub.StreamObserver<com.weather.proto.WeatherStats> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubscribeToStatsMethod(), responseObserver);
    }

    /**
     */
    default void subscribeToAlerts(com.weather.proto.Empty request,
        io.grpc.stub.StreamObserver<com.weather.proto.AlertMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSubscribeToAlertsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service AnalyticsService.
   */
  public static abstract class AnalyticsServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return AnalyticsServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service AnalyticsService.
   */
  public static final class AnalyticsServiceStub
      extends io.grpc.stub.AbstractAsyncStub<AnalyticsServiceStub> {
    private AnalyticsServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AnalyticsServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AnalyticsServiceStub(channel, callOptions);
    }

    /**
     */
    public void subscribeToStats(com.weather.proto.ReportRequest request,
        io.grpc.stub.StreamObserver<com.weather.proto.WeatherStats> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getSubscribeToStatsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void subscribeToAlerts(com.weather.proto.Empty request,
        io.grpc.stub.StreamObserver<com.weather.proto.AlertMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getSubscribeToAlertsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service AnalyticsService.
   */
  public static final class AnalyticsServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<AnalyticsServiceBlockingStub> {
    private AnalyticsServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AnalyticsServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AnalyticsServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.weather.proto.WeatherStats> subscribeToStats(
        com.weather.proto.ReportRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getSubscribeToStatsMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.weather.proto.AlertMessage> subscribeToAlerts(
        com.weather.proto.Empty request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getSubscribeToAlertsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service AnalyticsService.
   */
  public static final class AnalyticsServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<AnalyticsServiceFutureStub> {
    private AnalyticsServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AnalyticsServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AnalyticsServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_SUBSCRIBE_TO_STATS = 0;
  private static final int METHODID_SUBSCRIBE_TO_ALERTS = 1;

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
        case METHODID_SUBSCRIBE_TO_STATS:
          serviceImpl.subscribeToStats((com.weather.proto.ReportRequest) request,
              (io.grpc.stub.StreamObserver<com.weather.proto.WeatherStats>) responseObserver);
          break;
        case METHODID_SUBSCRIBE_TO_ALERTS:
          serviceImpl.subscribeToAlerts((com.weather.proto.Empty) request,
              (io.grpc.stub.StreamObserver<com.weather.proto.AlertMessage>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSubscribeToStatsMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.weather.proto.ReportRequest,
              com.weather.proto.WeatherStats>(
                service, METHODID_SUBSCRIBE_TO_STATS)))
        .addMethod(
          getSubscribeToAlertsMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              com.weather.proto.Empty,
              com.weather.proto.AlertMessage>(
                service, METHODID_SUBSCRIBE_TO_ALERTS)))
        .build();
  }

  private static abstract class AnalyticsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AnalyticsServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.weather.proto.WeatherProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AnalyticsService");
    }
  }

  private static final class AnalyticsServiceFileDescriptorSupplier
      extends AnalyticsServiceBaseDescriptorSupplier {
    AnalyticsServiceFileDescriptorSupplier() {}
  }

  private static final class AnalyticsServiceMethodDescriptorSupplier
      extends AnalyticsServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    AnalyticsServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (AnalyticsServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AnalyticsServiceFileDescriptorSupplier())
              .addMethod(getSubscribeToStatsMethod())
              .addMethod(getSubscribeToAlertsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
