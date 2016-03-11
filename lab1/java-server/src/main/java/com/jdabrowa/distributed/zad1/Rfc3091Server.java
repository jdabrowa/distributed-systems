package com.jdabrowa.distributed.zad1;

public interface Rfc3091Server {

    void bindPiDigitGeneration(int portNumber);
    void bind22by7DigitGeneration(int portNumber);

    void start();
}
