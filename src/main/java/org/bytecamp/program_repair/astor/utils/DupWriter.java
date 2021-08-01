package org.bytecamp.program_repair.astor.utils;

import java.util.ArrayList;

public class DupWriter implements Writer {
    ArrayList<Writer> writers = new ArrayList<>();

    public void add(Writer writer) {
        writers.add(writer);
    }

    @Override
    public void write(String chunk) {
         for (Writer writer : writers) {
            writer.write(chunk);
        }
    }
    @Override
    public void writeln(String chunk) {
        for (Writer writer : writers) {
            writer.writeln(chunk);
        }
    }
}
