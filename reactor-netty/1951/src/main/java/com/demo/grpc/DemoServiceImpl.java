package com.demo.grpc;

public class DemoServiceImpl extends DemoServiceGrpc.DemoServiceImplBase{

    @Override
    public void demo(com.google.protobuf.Empty request,
                     io.grpc.stub.StreamObserver<Services.Result> responseObserver) {
        Services.Result result= Services.Result.newBuilder().setReply("demo success").build();
        responseObserver.onNext(result);
        responseObserver.onCompleted();
    }
}
