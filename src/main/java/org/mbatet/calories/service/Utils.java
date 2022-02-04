package org.mbatet.calories.service;

import java.math.BigDecimal;

public class Utils {

    public static float round(float number)
    {
        float rounded = BigDecimal.valueOf(number)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();

        return rounded;
    }


    public static Float roundToTens(Float value)
    {
        if(value ==null)
        {
            return null;
        }

        float rounded = Math.round(value /10F)*10F;
        return rounded;
    }
}
