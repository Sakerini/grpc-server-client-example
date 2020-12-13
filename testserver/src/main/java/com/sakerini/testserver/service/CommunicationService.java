package com.sakerini.testserver.service;


import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import testservergrpc.CommunicationGrpc;
import testservergrpc.CommunicationOuterClass;

@GrpcService
public class CommunicationService extends CommunicationGrpc.CommunicationImplBase {
/*
    private List<CommunicationOuterClass.Numbers> getOrCreateNumbers(int numbers) {
        List<RouteNote> notes = Collections.synchronizedList(new ArrayList<RouteNote>());
        List<RouteNote> prevNotes = routeNotes.putIfAbsent(location, notes);
        return prevNotes != null ? prevNotes : notes;
    }

 */

    @Override
    public StreamObserver<CommunicationOuterClass.Numbers> exchangeNumbers(StreamObserver<CommunicationOuterClass.Numbers> responseObserver) {

        System.out.println("Exchanging Numbers");

        return new StreamObserver<CommunicationOuterClass.Numbers>() {
            int count = 0;
            int forSend = 0;


            @Override
            public void onNext(CommunicationOuterClass.Numbers numbers) {
                System.out.println("we Got number: " + numbers.getNumber());
                //send back nubmer + 1
                System.out.println("sending back number + 1 : " + (numbers.getNumber() + 1));
                responseObserver.onNext(CommunicationOuterClass.Numbers.newBuilder().setNumber(numbers.getNumber() + 1).build());

            }

            @Override
            public void onError(Throwable throwable) {
                //here we implement onError code
                System.out.println("error");
            }

            @Override
            public void onCompleted() {
                System.out.println("exchange completed");
                responseObserver.onCompleted();
            }
        };

    }

    @Override
    public void getNumbers(CommunicationOuterClass.Numbers request, StreamObserver<CommunicationOuterClass.Numbers> responseObserver) {
        System.out.println("Recieved stream request for " + request.getNumber() + " numbers");
        for (int i = 1; i <= request.getNumber(); i++) {
            CommunicationOuterClass.Numbers numbers = CommunicationOuterClass.Numbers.newBuilder().setNumber(i).build();
            responseObserver.onNext(numbers);
        }
        responseObserver.onCompleted();
    }

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
