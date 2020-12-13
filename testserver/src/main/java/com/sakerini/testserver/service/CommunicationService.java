package com.sakerini.testserver.service;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import testservergrpc.CommunicationGrpc;
import testservergrpc.CommunicationOuterClass;

@GrpcService
public class CommunicationService extends CommunicationGrpc.CommunicationImplBase {


    // Client - Side streaming
    @Override
    public StreamObserver<CommunicationOuterClass.Numbers> sendNumbers(StreamObserver<CommunicationOuterClass.BasicResponse> responseObserver) {
        return new StreamObserver<CommunicationOuterClass.Numbers>() {
            int count = 0;

            @Override
            public void onNext(CommunicationOuterClass.Numbers numbers) {
                //we get stream elements here
                count++;
                System.out.println(numbers.getNumber());
            }

            @Override
            public void onError(Throwable throwable) {
                //here we implement onError code
                System.out.println("error");
            }

            @Override
            public void onCompleted() {
                //returned response on server is here
                CommunicationOuterClass.BasicResponse basicResponse = CommunicationOuterClass.BasicResponse.newBuilder().setStatus("OK").build();
                responseObserver.onNext(basicResponse);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void login(CommunicationOuterClass.AccountInformation request, StreamObserver<CommunicationOuterClass.LoginResponse> responseObserver) {
        String username = request.getUsername();
        String password = request.getPassword();

        System.out.println("Recieved");
        if (username.equals("admin")) {
            if (password.equals("admin")) {
                CommunicationOuterClass.LoginResponse loginResponse = CommunicationOuterClass.LoginResponse.newBuilder().setStatus("OK").build();
                responseObserver.onNext(loginResponse);
                responseObserver.onCompleted();
                return;
            }
        }

        CommunicationOuterClass.LoginResponse loginResponse = CommunicationOuterClass.LoginResponse.newBuilder().setStatus("WRONG").build();
        responseObserver.onNext(loginResponse);
        responseObserver.onCompleted();
    }
}
