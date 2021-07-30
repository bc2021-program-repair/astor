#!/bin/sh
BASE_PATH=$(dirname $(realpath $0))
protoc --plugin=protoc-gen-grpc_java=$(which protoc-gen-grpc-java) -I=$BASE_PATH/src/main/proto --grpc_java_out=src/main/java --java_out=src/main/java $BASE_PATH/src/main/proto/*