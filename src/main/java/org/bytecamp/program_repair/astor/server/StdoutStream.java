package org.bytecamp.program_repair.astor.server;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class StdoutStream extends OutputStream {
    private WriteLine writer;
    private StringBuffer buffer = new StringBuffer();

    public StdoutStream(WriteLine writeLine) {
        this.writer = writeLine;
    }

    @Override
    public void write(int b) throws IOException {
        if (b == '\n') {
            if (buffer.length() > 0) {
                writer.writeln(buffer.toString());
                buffer.setLength(0);
            }
            return;
        }
        if (buffer.length() > 300) {
            writer.write(buffer.toString());
            buffer.setLength(0);
        }
        buffer.append(b);
    }

    @Override
    public void write(byte b[]) throws IOException {
        if (buffer.length() > 0) {
            writer.write(buffer.toString());
            buffer.setLength(0);
        }
        writer.write(new String(b, StandardCharsets.UTF_8));
    }

    public void write(byte b[], int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        if (buffer.length() > 0) {
            writer.write(buffer.toString());
            buffer.setLength(0);
        }
        writer.write(new String(Arrays.copyOfRange(b, off, off + len), StandardCharsets.UTF_8));
    }

    @Override
    public void flush() throws IOException {
        if (buffer.length() > 0) {
            writer.write(buffer.toString());
            buffer.setLength(0);
        }
    }

    @Override
    public void close() throws IOException {
        if (buffer.length() > 0) {
            writer.write(buffer.toString());
            buffer.setLength(0);
        }
    }
}
