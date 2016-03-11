package com.jdabrowa.distributed.zad1.math;

import java.math.BigDecimal;

public class PiCalculator {

    public int [] getDigits(int numberOfDigits) {

        final BigDecimal precission = new BigDecimal("0.1").pow(numberOfDigits+1);
        final BigDecimal one = BigDecimal.ONE;
        final BigDecimal two = new BigDecimal(2);

        BigDecimal currentDenominator = BigDecimal.ONE;
        BigDecimal currentElement = BigDecimal.ONE;
        BigDecimal pi = BigDecimal.ZERO;

        System.out.println(precission);

        while(precission.compareTo(currentElement) < 0) {
            pi = pi.add(currentElement);
            currentDenominator = currentDenominator.add(two);
            currentElement = one.divide(currentDenominator, numberOfDigits, BigDecimal.ROUND_DOWN);
            pi = pi.subtract(currentElement);
            currentDenominator = currentDenominator.add(two);
            currentElement = one.divide(currentDenominator, numberOfDigits, BigDecimal.ROUND_DOWN);
        }

        pi = pi.multiply(new BigDecimal("4"));

        System.out.println(pi.toString());

        return null;
    }

}
