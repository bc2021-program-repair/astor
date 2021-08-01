package org.bytecamp.program_repair.astor.utils;

public interface Writer {
    void write(String chunk);
    default void writeln(String line) {
        this.write(line + "\n");
    }
}
