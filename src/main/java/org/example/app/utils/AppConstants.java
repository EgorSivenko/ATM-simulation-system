package org.example.app.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class AppConstants {
    public static final int ONE_SECOND_IN_MILLIS = 1000;
    public static final int TWO_SECONDS_IN_MILLIS = 2000;
    public static final int BIG_DECIMAL_SCALE = 2;
    public static final int ATTEMPTS_AMOUNT = 3;
    public static final int ZERO = 0;

    public static final BigDecimal COMMISSION_PERCENTAGE = new BigDecimal(5);
    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);
}
