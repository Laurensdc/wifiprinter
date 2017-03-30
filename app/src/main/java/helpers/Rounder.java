package helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by uizen on 21/03/2017.
 */

public class Rounder {
    public static String round(double value) {
//        if (places < 0) throw new IllegalArgumentException();
//
//        BigDecimal bd = new BigDecimal(value);
//        bd = bd.setScale(places, RoundingMode.HALF_UP);
//        return bd.doubleValue();

        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(value).trim();

    }
}
