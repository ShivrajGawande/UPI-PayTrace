package com.upipaytrace.util;

import java.math.BigDecimal;

public class AmountParserUtil {

    private AmountParserUtil() {}

    public static BigDecimal parse(String value) {

        if (value == null)
            throw new IllegalArgumentException("Invalid amount");

        String amount = value.trim()
                .replace(",", "")
                .replace("-", "");

        if (amount.isEmpty() || amount.equalsIgnoreCase("NA"))
            throw new IllegalArgumentException("Invalid amount");

        return new BigDecimal(amount);
    }
}
