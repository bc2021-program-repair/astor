package org.bytecamp.program_repair.astor.server;

public interface Writer {
    void write(String chunk);
    default void writeln(String line) {
        this.write(line + "\n");
    }
}
