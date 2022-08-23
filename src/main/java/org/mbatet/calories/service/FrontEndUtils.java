package org.mbatet.calories.service;

import org.mbatet.calories.model.Constants;
import org.mbatet.calories.model.Interval;

public class FrontEndUtils {


    final static public String tableClass(int intervalType) {

        if (intervalType == Constants.TYPE_WEIGHT_LOSS) {
            return "table-success";
        }

        if (intervalType == Constants.TYPE_WEIGHT_MAINTENANCE) {
            return "table-warning";
        }


        return "table-danger";

    }


}



