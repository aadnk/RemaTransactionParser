package com.comphenix.rema1000.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class DataWriter<T> {
    /**
     * Write the given data to the output stream.
     * @param output the output stream.
     * @param data the data to write.
     */
    public abstract void write(Path output, T data) throws IOException;
}
