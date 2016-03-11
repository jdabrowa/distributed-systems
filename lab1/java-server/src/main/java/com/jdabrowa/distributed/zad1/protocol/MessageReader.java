package com.jdabrowa.distributed.zad1.protocol;

import java.io.IOException;

public interface MessageReader {

    Message readMessage() throws IOException;
}
