package org.bytecamp.program_repair.astor.server;

public interface WriteLine {
    void write(String chunk);
    default void writeln(String line) {
        this.write(line + "\n");
    }
}
