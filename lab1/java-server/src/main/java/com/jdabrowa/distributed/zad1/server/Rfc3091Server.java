package com.jdabrowa.distributed.zad1.server;

import java.io.IOException;

public interface Rfc3091Server {

    void bindPiDigitGeneration(int portNumber) throws IOException;
    void bind22by7DigitGeneration(int portNumber) throws IOException;

    void start();
}
