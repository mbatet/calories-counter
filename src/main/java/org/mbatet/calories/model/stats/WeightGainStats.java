package org.mbatet.calories.model.stats;

public class WeightGainStats extends AverageCalStats {


    public String getTitle(){return "Estimated/recommended cals for weight gain";}

    @Override
    public String toString()
    {

        return "[WeightGainStats]activityCals:" + activityCals + " - consumedCals: " + consumedCals + " - adjustedCals: " + adjustedCals;
    }


    /*
    public void changeRecomendedCalsIfBigger(Float value) {

        if(value==null || this.recomendedCals==null)
        {
            return;
        }

        if( value >this.recomendedCals) this.recomendedCals = value;
    }*/
}
