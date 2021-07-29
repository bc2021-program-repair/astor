package org.bytecamp.program_repair.astor.server;

import fr.inria.main.evolution.AstorMain;
import io.grpc.stub.StreamObserver;
import org.bytecamp.program_repair.astor.grpc.AstorLanguageServerGrpc;
import org.bytecamp.program_repair.astor.grpc.ExecuteRequest;
import org.bytecamp.program_repair.astor.grpc.ExecuteResponse;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AstorLanguageServerImpl extends AstorLanguageServerGrpc.AstorLanguageServerImplBase {
    AstorMain main = new AstorMain();
    public void execute(ExecuteRequest request, StreamObserver<ExecuteResponse> responseObserver) {
        String[] args = request.getArgsList().toArray(new String[0]);
        try {
            main.execute(args);
            responseObserver.onNext(ExecuteResponse.newBuilder().setArg("Done").build());
        } catch (Exception e) {
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            responseObserver.onNext(ExecuteResponse.newBuilder().setArg(sw.toString()).build());
        }
        responseObserver.onCompleted();
    }

}
