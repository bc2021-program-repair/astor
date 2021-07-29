#!/bin/sh
protoc --plugin=protoc-gen-grpc_java=$(which protoc-gen-grpc-java) -I=src/main/proto --grpc_java_out=src/main/java --java_out=src/main/java src/main/proto/*