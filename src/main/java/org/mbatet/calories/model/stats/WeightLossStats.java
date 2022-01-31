package org.mbatet.calories.model.stats;

public class WeightLossStats extends AverageCalStats {


    public String getTitle(){return "Estimated/recommended cals for weight loss";}

    @Override
    public String toString()
    {

        return "[WeightLossStats]activityCals:" + activityCals + " - consumedCals: " + consumedCals + " - adjustedCals: " + adjustedCals;
    }


    public void changeRecomendedCalsIfSmaller(Float value) {

        if(value==null || this.recomendedCals==null)
        {
            return;
        }

        if( value < this.recomendedCals) this.recomendedCals = value;
    }



}
