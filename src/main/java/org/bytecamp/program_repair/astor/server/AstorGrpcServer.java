package org.bytecamp.program_repair.astor.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import org.apache.log4j.Logger;


public class AstorGrpcServer {

    private static final Logger logger = Logger.getLogger(AstorGrpcServer.class);

    private final Server server;

    private AstorGrpcServer() {
        server = ServerBuilder.forPort(10000).addService(new AstorLanguageServerImpl()).build();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        AstorGrpcServer server = new AstorGrpcServer();
        server.start();
        server.blockUntilShutdown();
    }

    private void start() throws IOException {
        server.start();
        logger.info("Server started, listening on 10000 " + logger.isInfoEnabled());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            AstorGrpcServer.this.stop();
            System.err.println("*** server shut down");
        }));
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

}
