package com.jdabrowa.distributed.zad1.service;

import com.jdabrowa.distributed.zad1.math.PiProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleService implements Rfc3091Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleService.class);

    private final PiProvider piProvider;

    public SimpleService(PiProvider piProvider) {
        this.piProvider = piProvider;
    }

    @Override
    public int getPiNthDigit(int n) {
        int nthDigitOfPi = piProvider.getNthDigitOfPi(n);
        LOGGER.debug("Returning {} as {}-th digit of Pi", nthDigitOfPi, n);
        return nthDigitOfPi;
    }

    @Override
    public int get22by7Nthdigit(int n) {
        return piProvider.getNthDigitOfPi(n);
    }
}
