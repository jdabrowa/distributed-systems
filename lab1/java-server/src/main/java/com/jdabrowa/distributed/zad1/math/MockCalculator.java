package com.jdabrowa.distributed.zad1.math;

public class MockCalculator implements PiProvider {

    @Override
    public int getNthDigitOfPi(int n) {
        return n % 10;
    }
}
