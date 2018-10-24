package com.comphenix.rema1000.io;

import java.io.IOException;
import java.io.OutputStream;

public abstract class DataWriter<T> {
    /**
     * Write the given data to the output stream.
     * @param output the output stream.
     * @param data the data to write.
     */
    public abstract void write(OutputStream output, T data) throws IOException;
}
