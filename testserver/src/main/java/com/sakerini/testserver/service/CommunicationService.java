package com.sakerini.testserver.service;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import testservergrpc.CommunicationGrpc;
import testservergrpc.CommunicationOuterClass;

@GrpcService
public class CommunicationService extends CommunicationGrpc.CommunicationImplBase {
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
