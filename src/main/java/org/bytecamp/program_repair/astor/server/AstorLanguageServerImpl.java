package org.bytecamp.program_repair.astor.server;

import fr.inria.main.evolution.AstorMain;
import io.grpc.stub.StreamObserver;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.bytecamp.program_repair.astor.grpc.AstorLanguageServerGrpc;
import org.bytecamp.program_repair.astor.grpc.ExecuteRequest;
import org.bytecamp.program_repair.astor.grpc.ExecuteResponse;
import org.bytecamp.program_repair.astor.utils.DupWriter;
import org.bytecamp.program_repair.astor.utils.FileWriter;
import org.bytecamp.program_repair.astor.utils.Writer;
import org.bytecamp.program_repair.astor.utils.WriterStream;

import java.io.*;
import java.util.Arrays;

public class AstorLanguageServerImpl extends AstorLanguageServerGrpc.AstorLanguageServerImplBase {
    protected Logger logger = Logger.getLogger(AstorLanguageServerImpl.class);

    AstorMain main = new AstorMain();

    public void execute(ExecuteRequest request, StreamObserver<ExecuteResponse> responseObserver) {
        String[] args = request.getArgsList().toArray(new String[0]);
        PrintStream stdout = System.out;
        System.setOut(newPrintStream(responseObserver));
        logger.info("Executing astor with arguments " + Arrays.toString(args));

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
        logger.info("Executed astor with arguments " + Arrays.toString(args));
        responseObserver.onCompleted();
        System.setOut(stdout);
    }
    public static PrintStream newPrintStream(StreamObserver<ExecuteResponse> responseObserver) {
        DupWriter writer = new DupWriter();
        try {
            writer.add(new FileWriter(new File("/dev/stdout")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        writer.add(new ExecuteResponseWriter(responseObserver));
        return new PrintStream(new WriterStream(writer));
    }
}

class ExecuteResponseWriter implements Writer {
    StreamObserver<ExecuteResponse> observer;

    ExecuteResponseWriter(StreamObserver<ExecuteResponse> observer) {
        this.observer = observer;
    }

    @Override
    public void write(String chunk) {
        observer.onNext(ExecuteResponse.newBuilder().setFrameType(ExecuteResponse.FrameType.STDOUT).setArg(chunk).build());
    }

}