package org.bytecamp.program_repair.astor.server;

import fr.inria.main.evolution.AstorMain;
import io.grpc.stub.StreamObserver;
import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.bytecamp.program_repair.astor.grpc.AstorLanguageServerGrpc;
import org.bytecamp.program_repair.astor.grpc.ExecuteRequest;
import org.bytecamp.program_repair.astor.grpc.ExecuteResponse;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

public class AstorLanguageServerImpl extends AstorLanguageServerGrpc.AstorLanguageServerImplBase {
    protected Logger logger = Logger.getLogger(AstorLanguageServerImpl.class.getName());

    AstorMain main = new AstorMain();

    public void execute(ExecuteRequest request, StreamObserver<ExecuteResponse> responseObserver) {
        String[] args = request.getArgsList().toArray(new String[0]);
        PrintStream stdout = System.out;
        System.setOut(new PrintStream(new WriterStream(new Wrapper(responseObserver))));
        System.out.println("Executing astor with arguments " + Arrays.toString(args));
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();
        if (config.getAppender("stdout") == null) {
            StdoutAppender my_appender = StdoutAppender.createAppender("stdout");
            my_appender.start(); // this is optional
            config.addAppender(my_appender);  // this is optional
            ctx.getRootLogger().addAppender(my_appender);
            ctx.updateLoggers();
        }

        StdoutAppender.enabled = true;

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
        System.out.println("Executed astor with arguments " + Arrays.toString(args));
        responseObserver.onCompleted();
        StdoutAppender.enabled = false;
        System.setOut(stdout);
    }

}

class Wrapper implements Writer {
    StreamObserver<ExecuteResponse> observer;

    Wrapper(StreamObserver<ExecuteResponse> observer) {
        this.observer = observer;
    }

    @Override
    public void write(String chunk) {
        observer.onNext(ExecuteResponse.newBuilder().setFrameType(ExecuteResponse.FrameType.STDOUT).setArg(chunk).build());
    }

}