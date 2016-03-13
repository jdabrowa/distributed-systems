package com.jdabrowa.distributed.zad2;

import lombok.Getter;

public class FileRequest {

    @Getter private final String fileName;

    public FileRequest(String fileName) {
        this.fileName = fileName;
    }
}
